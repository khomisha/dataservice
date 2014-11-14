/*
 * Copyright 2013-2014 Mikhail Khodonov
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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.homedns.mkh.databuffer.DBTransaction;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.InvalidDatabufferDesc;
import org.homedns.mkh.dataservice.shared.Id;
import com.akiban.sql.StandardException;
import java.io.IOException;
import java.io.Serializable;
import javax.security.auth.callback.TextInputCallback;

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

	private Subject _subject;
    private CallbackHandler _callbackHandler;
    private Map< String, ? > _options;
    private String _sLogin;
    private Locale _locale;
    private String _sClientDatetimeFmt;
	
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
	    _subject = subject;
	    _callbackHandler = callbackHandler;
	    _options = options;
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	@Override
	public boolean login( ) throws LoginException {
        Callback[] callbacks = new Callback[] {
        	new NameCallback( "username" ),
        	new PasswordCallback( "password: ", false ),
        	new LanguageCallback( ),
        	new TextInputCallback( "dateTimeFmt" )
        };
        boolean bLogin = false;
        CheckupService cs = null;
        try {
			_callbackHandler.handle( callbacks );
			_sLogin = ( ( NameCallback )callbacks[ 0 ] ).getName( );
			String sPwd = String.valueOf( ( ( PasswordCallback )callbacks[ 1 ] ).getPassword( ) );
			_locale = ( ( LanguageCallback )callbacks[ 2 ] ).getLocale( );
			_sClientDatetimeFmt = ( ( TextInputCallback )callbacks[ 3 ] ).getText( );
			String sClassName = ( String )_options.get( "checkup_service_class" );
			LOG.info( "locale: " + _locale.getLanguage( ) );
			LOG.info( "datetime format: " + _sClientDatetimeFmt );
			LOG.debug( "checkup service class: " + sClassName );
			cs = ( CheckupService )Class.forName( sClassName ).newInstance( );
			cs.init( 
				_options, 
				getDataSource( "jdbc_login_resource_name" ), 
				_locale, 
				new SimpleDateFormat( _sClientDatetimeFmt ) 
			);
			if( !cs.isLocked( ) ) {
				cs.incrementCount( );
	        	bLogin = cs.isValidUser( _sLogin, sPwd );
	            if( bLogin ) {
	            	cs.resetCount( );
	            }
			}
        }
        catch( Exception che ) {
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
        	throw new FailedLoginException( 
        		Context.getBundle( _locale ).getString( "invalidLogin" ) 
        	); 
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
		DataBufferManager dbm = null;
		try {
			DataSource dataSource = getDataSource( "jdbc_db_resource_name" );
			dbm = new DataBufferManager( 
				new DBTransaction( dataSource ),
				_locale,
				new SimpleDateFormat( _sClientDatetimeFmt )
			);
			Context.getInstance( ).setDataBufferManager( dbm );
			// see user_access_right.dbuf for example
			Id accessDBId = new Id( );
			DataBuffer accessDB = getDataBuffer( dbm, "access_rights_db", accessDBId );
			accessDB.retrieve( Arrays.asList( new Serializable[] { _sLogin } ) );
			_subject.getPrincipals( ).add( new UserPrincipal( _sLogin ) );
			_subject.getPrincipals( ).add( new LocalePrincipal( _locale ) );
			_subject.getPrincipals( ).add( new AccessRightsPrincipal( accessDB, accessDBId ) );
		}
		catch( Exception e ) {
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
		_subject = null;
	    _sLogin = null;
	    _locale = null;
		return( true );
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	@Override
	public boolean logout( ) throws LoginException {	
		try {
			Context.getInstance( ).getDataBufferManager( ).close( );
		} catch( Exception e ) {
        	LoginException le = new LoginException( );
        	le.initCause( e );
        	throw le;
		}
		finally {
			_subject = null;
		    _sLogin = null;
		    _locale = null;
		}
		return( true );
	}

	/**
	 * Returns login
	 * 
	 * @return the login
	 */
	protected String getLogin( ) {
		return( _sLogin );
	}

	/**
	 * Returns data source.
	 * 
	 * @param sResourceName
	 *            the resource name
	 * 
	 * @return the data source
	 * 
	 * @throws NamingException
	 */
	private DataSource getDataSource( String sResourceName ) throws NamingException {
		javax.naming.Context initContext = new InitialContext( );
		javax.naming.Context envContext = ( javax.naming.Context )initContext.lookup( 
			"java:/comp/env" 
		);
		if( envContext == null ) {
			throw new ConfigurationException( 
				Context.getBundle( _locale ).getString( "noContext" ) 
			);
		}
		DataSource dataSource = ( DataSource )envContext.lookup( 
			( String )_options.get( sResourceName ) 
		);
		if( dataSource == null ) {
			throw new ConfigurationException( 
				Context.getBundle( _locale ).getString( "noDatasource" ) + ": " + sResourceName 
			);			
		}
		return( dataSource );
	}
	
	/**
	 * Returns data buffer
	 * 
	 * @param dbm
	 *            the data buffer manager
	 * @param sKey
	 *            the key word in JAAS configuration file defining data buffer
	 *            name
	 * @param id
	 *            the identification object
	 * 
	 * @return the data buffer
	 * 
	 * @throws InvalidDatabufferDesc 
	 * @throws StandardException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private DataBuffer getDataBuffer( 
		DataBufferManager dbm, 
		String sKey, 
		Id id 
	) throws InvalidDatabufferDesc, IOException, SQLException, StandardException {
		String sDataBufferName = ( String )_options.get( sKey );
		LOG.debug( "security service db's: " + sDataBufferName );
		id.setName( sDataBufferName );
		return( dbm.getDataBuffer( id ) );
	}
}
