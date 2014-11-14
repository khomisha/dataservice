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
 * $Id: RPCCall.java 6 2013-09-09 07:17:19Z khomisha $
 */

package org.homedns.mkh.dataservice.client;

import java.util.logging.Logger;
import org.homedns.mkh.dataservice.client.event.MaskEvent;
import org.homedns.mkh.dataservice.client.event.UnmaskEvent;
import org.homedns.mkh.dataservice.client.presenter.Presenter;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Util;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * RPC call
 *
 */
public class RPCCall {
	private static final Logger LOG = Logger.getLogger( RPCCall.class.getName( ) );  
	
	/**
	 * Executes RPC call
	 * 
	 * @param presenter
	 *            the presenter which submit call
	 */
	public void execute( final Presenter presenter ) {
        log( presenter.getRequest( ) );
		MaskEvent.fire( );
		AbstractEntryPoint.getDataService( ).doRPC( 
			presenter.getRequest( ), 
			new RPCCallBack( presenter )
		);		
	}
	
	private void log( Request request ) {
		if( request.getID( ) != null ) {
			LOG.config( getClass( ).getName( ) + ": execute: " + request.getID( ).toString( ) );
		}
		LOG.config( getClass( ).getName( ) + ": execute: " + request.getHandlerClassName( ) );		
	}
	
	/**
	 * AsyncCallback implementation {@link com.google.gwt.user.client.rpc.AsyncCallback}
	 */
	public class RPCCallBack implements AsyncCallback< Response > {
		private Presenter _presenter;
		
		/**
		 * @param presenter
		 *            the presenter which submit call
		 */
		public RPCCallBack( Presenter presenter ) {
			_presenter = presenter;
		}
		
		/**
		 * {@link com.google.gwt.user.client.rpc.AsyncCallback#onFailure(Throwable)}
		 */
		public void onFailure( Throwable caught ) {
			_presenter.setRegisterLock( false );
			UnmaskEvent.fire( );
			Util.signalMsg( caught, Util.MSG_BOX, getMsg( null ) );
		}
		
		/**
		 * {@link com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(Object)}
		 */
		public void onSuccess( Response response ) {
			_presenter.setRegisterLock( false );
			UnmaskEvent.fire( );
			if( isFailure( response ) ) {
				Util.signalMsg( null, Util.MSG_BOX, getMsg( response ) );
			} else {
				_presenter.onResponse( response );
			}
		}
		
		/**
		 * Returns true if data service method returns FAILURE and false otherwise
		 * 
		 * @param response
		 *            the RPC call method response object
		 * 
		 * @return true or false
		 */
		private boolean isFailure( Response response ) {
			return(	response.getResult( ) == Response.FAILURE );
		}
		
		/**
		 * Returns error message
		 * 
		 * @param response the response
		 * 
		 * @return the error message
		 */
		private String getMsg( Response response ) {
			String sHandlerClassName = _presenter.getRequest( ).getHandlerClassName( );
			return( response == null ? sHandlerClassName : sHandlerClassName + ": " + response.getError( ) );
		}
	}
}
