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

package org.homedns.mkh.dataservice.client.sender;

import org.homedns.mkh.dataservice.client.event.RPCallEvent;
import org.homedns.mkh.dataservice.client.view.View;
import org.homedns.mkh.dataservice.shared.Request;

/**
 * RPC request sender
 *
 */
public class RPCRequestSender implements RequestSender {
	private Request _request;
	private View _view;

	public RPCRequestSender( ) {}
	
	/**
	 * @see org.homedns.mkh.dataservice.client.sender.RequestSender#getRequest()
	 */
	@Override
	public Request getRequest( ) {
		return( _request );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.sender.RequestSender#send()
	 */
	@Override
	public void send( ) {
		if( _view != null ) {
			_view.onSend( _request );
		}
		RPCallEvent.fire( _request );			
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.sender.RequestSender#setRequest(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public void setRequest( Request request ) {
		_request = request;
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.sender.RequestSender#setView(org.homedns.mkh.dataservice.client.view.View)
	 */
	@Override
	public void setView( View view ) {
		_view = view;
	}
}
