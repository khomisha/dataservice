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

package org.homedns.mkh.dataservice.client.event;

import org.homedns.mkh.dataservice.shared.Request;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * RPC call event
 *
 */
public class RPCallEvent extends GenericEvent< RPCallEvent.RPCallHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface RPCallHandler extends EventHandler {
		public void onRPCall( RPCallEvent event );
	}
	  
	private Request _request;
	public static final Event.Type< RPCallHandler > TYPE = new Event.Type< RPCallHandler >( );

	/**
	 * Fires RPC call event
	 * 
	 * @param request
	 *            the RPC request
	 */
	public static void fire( Request request ) {
		RPCallEvent event = new RPCallEvent( );
		event.setRequest( request );
		fire( event, request.getID( ).hashCode( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< RPCallHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( RPCallHandler handler ) {
		handler.onRPCall( this );
	}

	/**
	 * Returns RPC request
	 * 
	 * @return the RPC request
	 */
	public Request getRequest( ) {
		return( _request );
	}

	/**
	 * Sets RPC request
	 * 
	 * @param request
	 *            the RPC request to set
	 */
	public void setRequest( Request request ) {
		_request = request;
	}
}
