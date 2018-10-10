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
 * $Id: RPCCall.java 6 2013-09-09 07:17:19Z khomisha $
 */

package org.homedns.mkh.dataservice.client;

import org.homedns.mkh.dataservice.client.event.MaskEvent;
import org.homedns.mkh.dataservice.client.event.RPCResponseEvent;
import org.homedns.mkh.dataservice.client.event.UnmaskEvent;
import org.homedns.mkh.dataservice.client.presenter.Presenter;
import org.homedns.mkh.dataservice.shared.ClosePagingConnResponse;
import org.homedns.mkh.dataservice.shared.GenericResponse;
import org.homedns.mkh.dataservice.shared.LoginResponse;
import org.homedns.mkh.dataservice.shared.LogoutResponse;
import org.homedns.mkh.dataservice.shared.RemoveDataBufferResponse;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * RPC call
 *
 */
public class RPCCall extends AbstractRPCCall{
	
	/**
	 * Executes RPC call
	 * 
	 * @param presenter
	 *            the presenter which submit call
	 */
	public void execute( final Presenter presenter ) {
		Request request = presenter.getRequest( );
        log( request );
		MaskEvent.fire( );
		AbstractEntryPoint.getDataService( ).doRPC( request, new RPCCallBack( presenter ) );		
	}
	
	/**
	 * AsyncCallback implementation {@link com.google.gwt.user.client.rpc.AsyncCallback}
	 */
	public class RPCCallBack implements AsyncCallback< Response > {
		private Presenter presenter;
		private String sHandlerClassName;
		
		/**
		 * @param presenter
		 *            the presenter which submit call
		 */
		public RPCCallBack( Presenter presenter ) {
			this.presenter = presenter;
			this.sHandlerClassName = presenter.getRequest( ).getHandlerClassName( );
		}
		
		/**
		 * {@link com.google.gwt.user.client.rpc.AsyncCallback#onFailure(Throwable)}
		 */
		public void onFailure( Throwable caught ) {
			presenter.setRegisterLock( false );
			UnmaskEvent.fire( );
			String sErr = getMsg( sHandlerClassName, caught );
			Response response = new GenericResponse( );
			response.setResult( Response.FAILURE );
			response.setError( sErr );
			if( 
				!( 
					response instanceof LogoutResponse || 
					response instanceof LoginResponse  ||
					response instanceof RemoveDataBufferResponse  ||
					response instanceof ClosePagingConnResponse 
				) 
			) {
				RPCResponseEvent.fire( presenter.getID( ), response );
			}
			signalMsg( caught, sErr );
		}
		
		/**
		 * {@link com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(Object)}
		 */
		public void onSuccess( Response response ) {
			presenter.setRegisterLock( false );
			UnmaskEvent.fire( );
			if( response.getResult( ) == Response.FAILURE ) {
				signalMsg( null, getMsg( sHandlerClassName, response ) );
			} else {
				presenter.onResponse( response );
				String sMsg = getMsg( null, response );
				if( sMsg != null ) {
					signalMsg( null, sMsg );						
				}
			}
			if( 
				!( 
					response instanceof LogoutResponse || 
					response instanceof LoginResponse  ||
					response instanceof RemoveDataBufferResponse  ||
					response instanceof ClosePagingConnResponse 
				) 
			) {
				RPCResponseEvent.fire( presenter.getID( ), response );
			}
		}
	}
}
