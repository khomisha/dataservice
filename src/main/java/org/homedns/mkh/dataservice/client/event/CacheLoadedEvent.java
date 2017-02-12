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

package org.homedns.mkh.dataservice.client.event;

import org.homedns.mkh.dataservice.shared.Id;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Cache loaded event
 *
 */
public class CacheLoadedEvent extends GenericEvent< CacheLoadedEvent.CacheLoadedHandler > {

	/**
	 * Implemented by objects that handle.
	 */
	public interface CacheLoadedHandler extends EventHandler {
		public void onCacheLoaded( CacheLoadedEvent event );
	}
	  
	public static final Event.Type< CacheLoadedHandler > TYPE = new Event.Type< CacheLoadedHandler >( );

	/**
	 * Fires cache loaded event
	 * 
	 * @param id
	 *            the event source - identification object
	 */
	public static void fire( Id id ) {
		fire( new CacheLoadedEvent( ), id.getUID( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< CacheLoadedHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( CacheLoadedHandler handler ) {
		handler.onCacheLoaded( this );
	}
}
