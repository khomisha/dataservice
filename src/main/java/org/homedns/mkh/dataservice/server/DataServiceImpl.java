/*
 * Copyright 2013-2018 Mikhail Khodonov
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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.DBConnectionUnavailableException;
import org.homedns.mkh.databuffer.api.DataBufferManager;
import org.homedns.mkh.dataservice.client.DataService;
import org.homedns.mkh.dataservice.server.handler.RequestHandler;
import org.homedns.mkh.dataservice.server.handler.RequestHandlerBuilder;
import org.homedns.mkh.dataservice.shared.GenericResponse;
import org.homedns.mkh.dataservice.shared.Id;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Util;
import org.homedns.mkh.util.Parameters;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The implementation of the RPC data service which runs on the server.
 */
@SuppressWarnings( "serial" )
public class DataServiceImpl extends RemoteServiceServlet implements DataService {
	private static final Logger LOG = Logger.getLogger( DataServiceImpl.class );
	
	private Properties properties;
	private ConcurrentHashMap< Class< ? >, RequestHandler > handlers = new ConcurrentHashMap< >( );
	private Path resourceFullPath;
	
	public DataServiceImpl( ) {
		try {
			Context.setInstance( this );
	   		Locale.setDefault( new Locale( "en" ) );
	   		logInfo( );
		}
		catch( Exception e ) {
			LOG.error( e.getMessage( ), e );
		}
	}

	/**
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init( ) throws ServletException {
		super.init( );
		try {
			resourceFullPath = Paths.get( getServletContext( ).getResource( "/" ).getPath( ), getResourcePath( ).toString( ) );
			LOG.debug( "resource path: " + resourceFullPath.toString( ) );
		}
		catch( Exception e ) {
			ServletException se = new ServletException( );
			se.initCause( e );
			throw se;
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.DataService#doRPC(org.homedns.mkh.dataservice.shared.Request)
	 */
	public Response doRPC( Request request ) {
		LOG.debug( "get request: " + request.getID( ) );
		Response result = null;
		String sMsg = null;
		try {
			Id id = request.getID( );
			sMsg = ( id == null ) ? request.getHandlerClassName( ) : request.getHandlerClassName( ) + ": " + id.toString( );
			LOG.debug( "doRPC: " + sMsg );
			RequestHandler handler = bindHandler( request );
			result =  handler.execute( request );
		}
		catch( Exception e ) {
			LOG.debug( e.getMessage( ), e );
			result = ( result == null ) ? new GenericResponse( ) : result;
			if( getDataBufferManager( ) == null ) {
				result.setResult( Response.FAILURE );
				sMsg = Context.getLocalizedMsg( "sessionClosed" );
				LOG.info( sMsg );
				result.setError( sMsg + " " + Context.getLocalizedMsg( "tryLogin" ) );				
			} else {
				if( e instanceof DBConnectionUnavailableException ) {
					sMsg = e.getMessage( ) + " " + Context.getLocalizedMsg( "tryLogin" );
				}
				setFailure( result, sMsg, e );					
			}
		}
		return( result );
	}
	
	/**
	 * Binds request with appropriate handler
	 * 
	 * @param request
	 *            the request to bind
	 * 
	 * @return the request handler
	 * 
	 * @throws Exception
	 */
	protected RequestHandler bindHandler( Request request ) throws Exception {
		RequestHandler handler = handlers.get( request.getClass( ) );
		if( handler == null ) {
			handler = RequestHandlerBuilder.build( request );
			handlers.put( request.getClass( ), handler );
		}
		return( handler );
	}

	/**
	* Returns http servlet request.
	*
	* @return the http servlet request
	*/
	public HttpServletRequest getHttpServletRequest( ) {
		return( getThreadLocalRequest( ) );
	}
	
	/**
	* Returns http servlet response.
	*
	* @return the http servlet response
	*/
	public HttpServletResponse getHttpServletResponse( ) {
		return( getThreadLocalResponse( ) );
	}

	/**
	 * Returns current session login context
	 * 
	 * @return the login context
	 */
	public LoginContext getLoginContext( ) {
		return( ( LoginContext )getSessionAttribute( LoginContext.class.getName( ) ) );
	}
	
	/**
	 * Sets current session login context
	 * 
	 * @param lc
	 *            the login context to set
	 */
	public void setLoginContext( LoginContext lc ) {
		setSessionAttribute( LoginContext.class.getName( ), lc );
	}
	
	/**
	 * Returns current session data buffer manager
	 * 
	 * @return the data buffer manager
	 */
	public DataBufferManager getDataBufferManager( ) {
		return( ( DataBufferManager )getSessionAttribute( DataBufferManager.class.getName( ) ) );
	}
	
	/**
	 * Sets current session data buffer manager
	 * 
	 * @param dbm
	 *            the data buffer manager to set
	 */
	public void setDataBufferManager( DataBufferManager dbm ) {
		setSessionAttribute( DataBufferManager.class.getName( ), dbm );
	}

	/**
	 * Returns data service properties
	 * 
	 * @return the data service properties
	 */
	public Properties getProperties( ) {
		return( properties );
	}

	/**
	 * Returns full resource path
	 * 
	 * @return the full resource path
	 */
	public Path getFullResourcePath( ) {
		if( resourceFullPath == null ) {
			throw new IllegalArgumentException( Context.getLocalizedMsg( "noResourcePath" ) );			
		}
		return( resourceFullPath );
	}

	/**
	 * Returns request client IP
	 * 
	 * @return the request client IP
	 */
	public String getRequestIp( ) {
		return( getHttpServletRequest( ).getRemoteAddr( ) );
	}
	
	/**
	 * Sets failure to the response and log it.
	 * 
	 * @param result
	 *            the response
	 * @param sMsg
	 *            the additional message
	 * @param e
	 *            the exception object
	 */
	protected void setFailure( Response result, String sMsg, Exception e ) {
		sMsg = ( sMsg == null ) ? "" : sMsg;
		LOG.error( sMsg, e );
		if( result != null ) {
			result.setResult( Response.FAILURE );
			result.setError( sMsg + ": " + Util.getCauseMsg( e ) );
		}
	}
	
	/**
	 * Returns session attribute value
	 * 
	 * @param sAttribute
	 *            the attribute name
	 * 
	 * @return the session attribute value
	 */
	public Object getSessionAttribute( String sAttribute ) {
		HttpSession session = getHttpServletRequest( ).getSession( );
		return( session.getAttribute( sAttribute ) );		
	}
	
	/**
	 * Sets attribute value
	 * 
	 * @param sAttribute
	 *            the attribute name
	 * @param value
	 *            the attribute value to set
	 */
	public void setSessionAttribute( String sAttribute, Object value ) {
		HttpSession session = getHttpServletRequest( ).getSession( );
		session.setAttribute( sAttribute, value );		
	}

	/**
	 * Loads properties file
	 * 
	 * @param sPropertyFilename
	 *            the properties file name
	 * 
	 * @throws IOException
	 */
	protected void loadProperties( Path path ) throws IOException {
		Parameters params = new Parameters( path );
		properties = params.getParameters( );
		LOG.debug( properties );
	}
	
	/**
	 * Logs application information
	 */
	protected void logInfo( ) {
	}

	/**
	 * Returns the resource path relative to the root of the web application, must override in descendant
	 * 
	 * @return the relative resource path
	 */
	protected Path getResourcePath( ) {
		return( null );
	}
}
