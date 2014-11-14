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
 * Logout event
 *
 */
public class LogoutEvent extends GenericEvent< LogoutEvent.LogoutHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface LogoutHandler extends EventHandler {
		public void onLogout( LogoutEvent event );
	}
	  
	private Request _request;
	public static final Event.Type< LogoutHandler > TYPE = new Event.Type< LogoutHandler >( );

	/**
	 * Fires logout event
	 * 
	 * @param request the event data
	 */
	public static void fire( Request request ) {
		LogoutEvent event = new LogoutEvent( );
		event.setRequest( request );
		fire( event, null );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< LogoutHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( LogoutHandler handler ) {
		handler.onLogout( this );
	}

	/**
	 * Returns logout request
	 * 
	 * @return the logout request
	 */
	public Request getRequest( ) {
		return( _request );
	}

	/**
	 * Sets logout request
	 * 
	 * @param request
	 *            the logout request to set
	 */
	public void setRequest( Request request ) {
		_request = request;
	}
}
