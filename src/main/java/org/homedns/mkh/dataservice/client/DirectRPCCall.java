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

import org.homedns.mkh.dataservice.client.event.MaskEvent;
import org.homedns.mkh.dataservice.client.event.RPCResponseEvent;
import org.homedns.mkh.dataservice.client.event.UnmaskEvent;
import org.homedns.mkh.dataservice.shared.ReportResponse;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.StoredProcResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Direct (without presenter object) RPC call
 *
 */
public class DirectRPCCall extends AbstractRPCCall {
	private Request request;
	
	/**
	 * Executes RPC call
	 * 
	 * @param request
	 *            the request to submit
	 */
	public void execute( Request request ) {
		setRequest( request );
        log( request );
		MaskEvent.fire( );
		AbstractEntryPoint.getDataService( ).doRPC( request, new RPCCallBack( ) );		
	}
	
	/**
	 * Returns request
	 * 
	 * @return the request
	 */
	public Request getRequest( ) {
		return( request );
	}

	/**
	 * Sets request
	 * 
	 * @param request the request to set
	 */
	public void setRequest( Request request ) {
		this.request = request;
	}

	/**
	 * AsyncCallback implementation {@link com.google.gwt.user.client.rpc.AsyncCallback}
	 */
	public class RPCCallBack implements AsyncCallback< Response > {
		
		public RPCCallBack( ) {}
		
		/**
		 * {@link com.google.gwt.user.client.rpc.AsyncCallback#onFailure(Throwable)}
		 */
		public void onFailure( Throwable caught ) {
			UnmaskEvent.fire( );
			signalMsg( caught, getMsg( request.getHandlerClassName( ), caught ) );
		}
		
		/**
		 * {@link com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(Object)}
		 */
		public void onSuccess( Response response ) {
			UnmaskEvent.fire( );
			if( response.getResult( ) == Response.FAILURE ) {
				signalMsg( null, getMsg( request.getHandlerClassName( ), response ) );
			} else {
				if( response instanceof ReportResponse || response instanceof StoredProcResponse ) {
					String sMsg = getMsg( null, response );
					if( sMsg != null ) {
						signalMsg( null, sMsg );						
					}
					RPCResponseEvent.fire( response.getID( ), response );
				}			
			}
		}
	}
}
