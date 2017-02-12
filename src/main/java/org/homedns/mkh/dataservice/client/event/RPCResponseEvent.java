/*
 * Copyright 2016 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.client.event;

import org.homedns.mkh.dataservice.client.event.GenericEvent;
import org.homedns.mkh.dataservice.shared.Id;
import org.homedns.mkh.dataservice.shared.Response;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * RPC response event
 *
 */
public class RPCResponseEvent extends GenericEvent< RPCResponseEvent.RPCResponseHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface RPCResponseHandler extends EventHandler {
		public void onRPCResponse( RPCResponseEvent event );
	}
	  
	private Response response;
	public static final Event.Type< RPCResponseHandler > TYPE = new Event.Type< RPCResponseHandler >( );

	/**
	 * Fires RPC response event
	 * 
	 * @param id the identification object
	 * @param response the RPC response
	 */
	public static void fire( Id id, Response response ) {
		RPCResponseEvent event = new RPCResponseEvent( );
		event.setResponse( response );
		fire( event, id.getUID( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< RPCResponseHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( RPCResponseHandler handler ) {
		handler.onRPCResponse( this );
	}

	/**
	 * Returns response
	 * 
	 * @return the response
	 */
	public Response getResponse( ) {
		return( response );
	}

	/**
	 * Sets response
	 * 
	 * @param response the response to set
	 */
	public void setResponse( Response response ) {
		this.response = response;
	}
}
