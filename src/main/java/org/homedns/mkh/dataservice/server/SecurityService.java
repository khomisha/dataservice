/*
 * Copyright 2013-2022 Mikhail Khodonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * $Id$
 */

package org.homedns.mkh.dataservice.server;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import javax.naming.ConfigurationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.api.DBMFactory;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.databuffer.api.DataBufferManager;
import org.homedns.mkh.databuffer.api.DataSourceWrapper;
import org.homedns.mkh.databuffer.api.GenericDataSource;
import org.homedns.mkh.dataservice.shared.Id;
import java.io.Serializable;

/**
 * Application login module. JAAS configuration file example (for Tomcat): <br>
 * 1. jaas.config should be placed in $CATALINA_HOME/conf/<br> 
 * 2. in $CATALINA_HOME/bin/catalina.sh must be added the following
 * <pre> 
 * JAVA_OPTS="$JAVA_OPTS "-Djava.security.auth.login.config=="$CATALINA_HOME/conf/jaas.config"
 * </pre>
 * or issue command
 * <pre>
 * export JAVA_OPTS=-Djava.security.auth.login.config==$CATALINA_HOME/conf/jaas.config
 * </pre> 
 * 3. jaas.config content
 * <pre> 
 * yourAppName {
 *     org.homedns.mkh.dataservice.server.SecurityService required
 *     jdbc_login_resource_name="jdbc/login"
 *     jdbc_db_resource_name="jdbc/db"
 *     login_db="yourLoginDataBufferName"
 *     access_rights_db="yourAccessRightsDatabufferName"
 *     checkup_service_class="yourCheckupServiceClass"
 *     attempt_threshold=3
 * };
 * </pre>
 * 4. Tomcat datasource definition example (postgresql). Database user for
 * login should has minimun sufficient access rights e.g. user table readonly
 * In META-INF/context.xml following resources must be defined:
 * <pre> 
 * &#060;Context&#062;
 *     &#060;!-- Specify a JDBC datasource for login--&#062;
 *     &#060;Resource name="jdbc/login" auth="Container"
 *         type="javax.sql.DataSource" username="DB_USERNAME" password="DB_PASSWORD"
 *         driverClassName="org.postgresql.Driver"
 *         url="jdbc:postgresql://localhost:5432/yourDatabaseName"
 *         maxActive="20" maxIdle="3" /&#062;
 * 
 *    &#060;!-- Specify a JDBC datasource for application--&#062;
 *    &#060;Resource name="jdbc/db" auth="Container"
 *         type="javax.sql.DataSource" username="DB_USERNAME1" password="DB_PASSWORD1"
 *         driverClassName="org.postgresql.Driver"
 *         url="jdbc:postgresql://localhost:5432/yourDatabaseName"
 *         maxActive="20" maxIdle="3" /&#062;
 * &#060;/Context&#062;
 * </pre>
 * 5. To get datasource should do something like this, 
 * (options specified in the jaas.config):
 * <pre>
 * javax.naming.Context initContext = new InitialContext( );
 * javax.naming.Context envContext = ( javax.naming.Context )initContext.lookup( 
 *		"java:/comp/env" 
 * );
 * ...
 * DataSource dataSource = ( DataSource )envContext.lookup( 
 *    	( String )options.get( "jdbc_login_resource_name" ) 
 * );
 * </pre> 
 * 6. Your jdbc driver must be placed to the $CATALINA_HOME/common/lib
 */
public class SecurityService implements LoginModule {
	private static final Logger LOG = Logger.getLogger( SecurityService.class );

	private Subject subject;
    private CallbackHandler callbackHandler;
    private Map< String, ? > options;
    private Callback[] callbacks;
	
	/**
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	@Override
	public void initialize( 
		Subject subject, 
		CallbackHandler callbackHandler,
		Map< String, ? > sharedState, 
		Map< String, ? > options 
	) {
	    this.subject = subject;
	    this.callbackHandler = callbackHandler;
	    this.options = options;
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	@Override
	public boolean login( ) throws LoginException {
        callbacks = new Callback[] {
        	new NameCallback( "username" ),
        	new PasswordCallback( "password: ", false ),
        	new LanguageCallback( )
         };
        boolean bLogin = false;
        CheckupService cs = null;
        try {
			callbackHandler.handle( callbacks );
			String sClassName = ( String )options.get( "checkup_service_class" );
			LOG.info( "locale: " + getLocale( ).getLanguage( ) );
			LOG.debug( "checkup service class: " + sClassName );
			DataBufferManager dbm = getDataBufferMgr( );
			cs = ( CheckupService )Class.forName( sClassName ).newInstance( );
			cs.init( options, dbm );
			if( !cs.isLocked( ) ) {
				cs.incrementCount( );
	        	bLogin = cs.isValidUser( getLogin( ), getPass( ) );
	            if( bLogin ) {
	            	cs.resetCount( );
	            }
			}
        }
        catch( Exception che ) {
        	LOG.debug( che.getMessage( ), che );
        	LoginException le = new LoginException( );
        	le.initCause( che );
        	throw le;
        }
        finally {
        	if( cs != null ) {
        		cs.close( );
        	}
        }
        if( !bLogin ) {
        	throw new FailedLoginException( Context.getLocalizedMsg( "invalidLogin" ) ); 
        }
       	return( bLogin );
	}

	/**
	 * 
	 * Example how to set session parameters in descendant
	 * <p>
	 * <pre>
	 * public boolean commit( ) throws LoginException {
	 *     boolean bCommit = super.commit( );
	 *     if( bCommit ) {
	 *         SessionParameters sp = new SessionParametersImpl( );
	 *         sp.setParameter( SessionParameters.CURRENT_LOGIN, &#060;DBMS current login name&#062; );
	 *         sp.setParameterValue( SessionParameters.CURRENT_LOGIN, sLogin );
	 *         env.getTransObject( ).setSessionParameters( sp );
	 *     }
	 *     return( bCommit );
	 * }
	 * </pre>
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	@Override
	public boolean commit( ) throws LoginException {
		DBManager dbm = null;
		try {
			dbm = ( DBManager )getDataBufferMgr( );
			dbm.addDataSource( getDataSource( "jdbc_db_resource_name" ) );
			subject.getPrincipals( ).add( new UserPrincipal( getLogin( ) ) );
			subject.getPrincipals( ).add( new LocalePrincipal( getLocale( ) ) );
			// see user_access_right.dbuf for example
			Id id = new Id( ( String )options.get( "access_rights_db" ) );
			DataBuffer accessDB = dbm.getDataBuffer( id );
			accessDB.retrieve( Arrays.asList( new Serializable[] { getLogin( ) } ) );
			subject.getPrincipals( ).add( new AccessRightsPrincipal( accessDB, id ) );
		}
		catch( Exception e ) {
        	LOG.debug( e.getMessage( ), e );
			if( dbm != null ) {
				dbm.close( );
			}
        	LoginException le = new LoginException( );
        	le.initCause( e );
        	throw le;
		}
		return( true );
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	@Override
	public boolean abort( ) throws LoginException {
		subject = null;
		return( true );
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	@Override
	public boolean logout( ) throws LoginException {
		DBManager dbm = null;
		try {
			dbm = ( DBManager )Context.getInstance( ).getDataBufferManager( );
			if( dbm != null ) {
				LOG.debug( "user: " + getLogin( ) + " logout" );
				dbm.close( );
			}
		} catch( Exception e ) {
        	LOG.debug( e.getMessage( ), e );
        	LoginException le = new LoginException( );
        	le.initCause( e );
        	throw le;
		}
		finally {
			subject = null;
			if( dbm != null ) {
				dbm = null;
			}
		}
		return( true );
	}

	/**
	 * Returns subject, @see javax.security.auth.Subject
	 * 
	 * @return the subject
	 */
	protected Subject getSubject( ) {
		return( subject );
	}
	
	/**
	 * Returns input parameters
	 * 
	 * @return the input parameters callback array
	 */
	protected Callback[] getInputParams( ) {
		return( callbacks );
	}
	
	/**
	 * Returns options defined in jaas.config
	 * 
	 * @return the options
	 */
	protected Map< String, ? > getOptions( ) {
		return( options );
	}
	
	/**
	 * Returns input login
	 * 
	 * @return the login
	 */
	protected String getLogin( ) {
		return( ( ( NameCallback )callbacks[ 0 ] ).getName( ) );
	}

	/**
	 * Returns input password
	 * 
	 * @return the password
	 */
	protected String getPass( ) {
		return( String.valueOf( ( ( PasswordCallback )callbacks[ 1 ] ).getPassword( ) ) );
	}
	
	/**
	 * Returns client locale
	 * 
	 * @return the locale
	 */
	protected Locale getLocale( ) {
		return( ( ( LanguageCallback )callbacks[ 2 ] ).getLocale( ) );
	}
	
	/**
	 * Returns data source.
	 * 
	 * @param sResource
	 *            the resource name
	 * 
	 * @return the data source
	 * 
	 * @throws NamingException
	 */
	protected GenericDataSource getDataSource( String sResource ) throws NamingException {
		javax.naming.Context initContext = new InitialContext( );
		javax.naming.Context envContext = ( javax.naming.Context )initContext.lookup( "java:/comp/env" );
		if( envContext == null ) {
			throw new ConfigurationException( Context.getLocalizedMsg( "noContext" ) );
		}
		DataSource ds = ( DataSource )envContext.lookup( ( String )options.get( sResource ) );
		if( ds == null ) {
			throw new ConfigurationException( Context.getLocalizedMsg( "noDatasource" ) + ": " + sResource );			
		}
		return( new DataSourceWrapper( ( String )options.get( sResource ), ds ) );
	}
	
	/**
	 * Returns data buffer manager
	 * 
	 * @return the data buffer manager
	 * 
	 * @throws Exception
	 */
	protected DataBufferManager getDataBufferMgr( ) throws Exception {
		DataBufferManager dbm = Context.getInstance( ).getDataBufferManager( );
		if( dbm == null ) {
			GenericDataSource ds = getDataSource( "jdbc_login_resource_name" );
			dbm = DBMFactory.create( DBManager.class, Arrays.asList( new GenericDataSource[] { ds } ) );
			dbm.setLocale( getLocale( ) );
			dbm.setResource( Context.getInstance( ).getFullResourcePath( ), false, null );
			Context.getInstance( ).setDataBufferManager( dbm );
		}
		return( dbm );
	}
}
