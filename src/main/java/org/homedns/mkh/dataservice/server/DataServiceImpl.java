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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.log4j.Logger;
import org.homedns.mkh.dataservice.client.DataService;
import org.homedns.mkh.dataservice.server.handler.RequestHandler;
import org.homedns.mkh.dataservice.server.handler.RequestHandlerBuilder;
import org.homedns.mkh.dataservice.shared.GenericResponse;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Util;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The implementation of the RPC data service which runs on the server.
 */
@SuppressWarnings( "serial" )
public class DataServiceImpl extends RemoteServiceServlet implements DataService {
	private static final Logger LOG = Logger.getLogger( DataServiceImpl.class );
	
	private Properties _properties;
	private Map< Class< ? >, RequestHandler > _handlers = new HashMap< Class< ? >, RequestHandler >( );
	private String _sSrvResourcePath;
	
	public DataServiceImpl( ) {
		try {
			Context.setInstance( this );
	   		Locale.setDefault( new Locale( "en" ) );
		}
		catch( Exception e ) {
			LOG.error( e.getMessage( ) );
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.DataService#doRPC(org.homedns.mkh.dataservice.shared.Request)
	 */
	public Response doRPC( Request request ) {
		Response result = null;
		String s = (
			request.getID( ) == null ? 
			request.getHandlerClassName( ) : 
			request.getID( ).toString( )
		);
		LOG.debug( getClass( ).getName( ) + ": doRPC: " + s );
		try {
			RequestHandler handler = bindHandler( request );
			result =  handler.execute( request );
		}
		catch( Exception e ) {
			result = ( result == null ) ? new GenericResponse( ) : result;
			String sMsg = (
				request.getID( ) == null ? 
				request.getHandlerClassName( ) : 
				request.getID( ).getName( )
			);
			setFailure( result, sMsg, e );
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
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected RequestHandler bindHandler( 
		Request request 
	) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		RequestHandler handler = _handlers.get( request.getClass( ) );
		if( handler == null ) {
			handler = RequestHandlerBuilder.build( request );
			_handlers.put( request.getClass( ), handler );
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
		return( _properties );
	}

	/**
	 * Returns server resource path
	 * 
	 * @return the server resource path
	 */
	public String getSrvResourcePath( ) {
		if( _sSrvResourcePath == null || "".equals( _sSrvResourcePath ) ) {
			throw new IllegalArgumentException( 
				Context.getBundle( getDataBufferManager( ).getLocale( ) ).getString( "noResourcePath" ) 
			);			
		}
		return( _sSrvResourcePath );
	}

	/**
	 * Sets server resource path
	 * 
	 * @param sSrvResourcePath
	 *            the server resource path to set
	 */
	public void setSrvResourcePath( String sSrvResourcePath ) {
		_sSrvResourcePath = sSrvResourcePath;
	}

	/**
	 * Returns data buffer description filename
	 * 
	 * @param sDataBufferName
	 *            the data buffer name
	 * 
	 * @return the data buffer description filename
	 */
	public String getDataBufferFilename( String sDataBufferName ) {
		String sFileName = getSrvResourcePath( ) + sDataBufferName + ".dbuf";
		if( sFileName == null || "".equals( sFileName ) ) {
			throw new IllegalArgumentException( sDataBufferName );
		}
		return( sFileName );
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
	protected Object getSessionAttribute( String sAttribute ) {
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
	protected void setSessionAttribute( String sAttribute, Object value ) {
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
	protected void loadProperties( String sPropertyFilename ) throws IOException {
		_properties = new Properties( );
		_properties.load( new FileInputStream( sPropertyFilename ) );
	}
}
