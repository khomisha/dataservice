/*
 * Copyright 2014 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.server.handler;

import java.util.Set;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.dataservice.server.AccessRightsPrincipal;
import org.homedns.mkh.dataservice.server.Context;
import org.homedns.mkh.dataservice.server.LoginCallbackHandler;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.LoginRequest;

/**
 * Login request handler
 *
 */
public class LoginHandler extends GenericRequestHandler {

	public LoginHandler( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		Response response = createResponse( request );
		try {
			LoginRequest loginRequest = ( LoginRequest )request;
			String sAppName = Context.getInstance( ).getProperties( ).getProperty( "APP_NAME" );
			LoginContext lc = new LoginContext( 
				sAppName, 
				new LoginCallbackHandler( 
					loginRequest.getLogin( ),
					loginRequest.getPassword( ),
					loginRequest.getLocale( ),
					loginRequest.getDateTimeFormat( )
				) 
			);
			lc.login( );
			Context.getInstance( ).setLoginContext( lc );
			Set< AccessRightsPrincipal > rights = lc.getSubject( ).getPrincipals( AccessRightsPrincipal.class ); //???
			AccessRightsPrincipal[] accessRights = rights.toArray( new AccessRightsPrincipal[ rights.size( ) ] );
			DataBuffer db = accessRights[ 0 ].getDataBuffer( );
			response.setDataBufferDesc( db.getDescriptionAsJson( ) );
			response.setID( accessRights[ 0 ].getID( ) );
			response.setData( db.getData( ) );
			response.setResult( Response.SUCCESS );
		}
		catch( FailedLoginException e ) {
			response.setResult( Response.FAILURE );
			response.setError( e.getMessage( ) );
		}
		return( response );
	}
}
