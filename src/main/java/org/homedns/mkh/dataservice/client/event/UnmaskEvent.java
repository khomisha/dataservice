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
 * $Id: UnmaskEvent.java 6 2013-09-09 07:17:19Z khomisha $
 */

package org.homedns.mkh.dataservice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Unmask event
 *
 */
public class UnmaskEvent extends GenericEvent< UnmaskEvent.UnmaskHandler > {

	/**
	 * Implemented by objects that handle.
	 */
	public interface UnmaskHandler extends EventHandler {
		public void onUnmask( UnmaskEvent event );
	}
	  
	public static final Event.Type< UnmaskHandler > TYPE = new Event.Type< UnmaskHandler >( );

	/**
	 * Fires unmask event
	 */
	public static void fire( ) {
		fire( new UnmaskEvent( ), null );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< UnmaskHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( UnmaskHandler handler ) {
		handler.onUnmask( this );
	}
}
