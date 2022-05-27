/*
 * Copyright 2017 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.client;

import java.util.logging.Logger;

import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Util;

/**
 * Abstract rpc call object
 * 
 */
public abstract class AbstractRPCCall {
	private static final Logger LOG = Logger.getLogger( AbstractRPCCall.class.getName( ) );

	/**
	 * Request execution debug logging
	 * 
	 * @param request
	 *            the request
	 */
	protected void log( Request request ) {
		if( request.getID( ) != null ) {
			LOG.config( LOG.getName( ) + ": execute: " + request.getID( ).toString( ) );
		}
		LOG.config( LOG.getName( ) + ": execute: " + request.getHandlerClassName( ) );
	}

	/**
	 * Returns message
	 * 
	 * @param sHandlerClassName
	 *            the handler class name
	 * @param source
	 *            the source object
	 * 
	 * @return the message or null
	 */
	protected String getMsg( String sHandlerClassName, Object source ) {
		String sMsg = null;
		if( source instanceof Response ) {
			Response src = ( Response )source;
			if( src.getResult( ) == Response.FAILURE ) {
				sMsg = Util.getCauseMsg( null );
				sMsg = ( 
					src == null ? 
					sHandlerClassName + ": " + sMsg : 
					sHandlerClassName + ": " + src.getError( ) 
				);
				LOG.severe( sMsg );
			} else {
				if( src.getReturnValueAsString( 1 ) != null ) {
					sMsg = src.getReturnValueAsString( 1 );
					LOG.info( LOG.getName( ) + ": " + sMsg );
				}
			}
		} else if( source instanceof Throwable ) {
			sMsg = sHandlerClassName + ": " + Util.getCauseMsg( ( Throwable )source );
			LOG.severe( LOG.getName( ) + ": " + sMsg );
		}
		return( sMsg );
	}

	/**
	 * Outputs error message using message box
	 * 
	 * @param caught
	 *            the throwable object
	 * @param sMsg
	 *            the additional error information
	 */
	protected void signalMsg( Throwable caught, String sMsg ) {
		Util.signalMsg( caught, Util.MSG_BOX, sMsg );
	}
}
