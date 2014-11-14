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
 * $Id: GenericEvent.java 6 2013-09-09 07:17:19Z khomisha $
 */

package org.homedns.mkh.dataservice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Generic event object.
 *
 */
public abstract class GenericEvent< H extends EventHandler > extends Event< H > {

	/**
	 * @see com.google.web.bindery.event.shared.Event#getAssociatedType()
	 */
	@Override
	public Event.Type< H > getAssociatedType( ) {
		return( getType( ) );
	}

	/**
	 * @see com.google.web.bindery.event.shared.Event#dispatch(java.lang.Object)
	 */
	@Override
	protected abstract void dispatch( H handler );
	
	/**
	 * Fires the given event to the handlers listening to the event's type.
	 * 
	 * @param event
	 *            the event to fire
	 * @param source
	 *            the event source object
	 */
	public static void fire( Event< ? > event, Object source ) {
		if( source == null ) {
			EventBus.getInstance( ).fireEvent( event );
		} else {
			EventBus.getInstance( ).fireEventFromSource( event, source );
		}
	}
	
	/**
	 * Returns event type
	 * 
	 * @return the event type
	 */
	protected abstract Event.Type< H > getType( );
}
