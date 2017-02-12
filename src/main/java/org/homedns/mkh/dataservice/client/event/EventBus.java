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
 * $Id: EventBus.java 6 2013-09-09 07:17:19Z khomisha $
 */

package org.homedns.mkh.dataservice.client.event;

import java.util.ArrayList;
import java.util.List;
import org.homedns.mkh.dataservice.client.presenter.DataPresenter;
import org.homedns.mkh.dataservice.client.presenter.PresenterFactory;
import com.google.web.bindery.event.shared.Event;

/**
 * Dispatches events between application's objects
 *
 */
public class EventBus extends BaseEventBus {
	private List< Object > _sourceIds = new ArrayList< Object >( );
	private static EventBus _instance = new EventBus( );

	private EventBus( ) { }

	/**
	 * Returns the event bus instance
	 * 
	 * @return the event bus instance
	 */
	public static EventBus getInstance( ) {
		return( _instance );
	}

	/**
	 * @see com.google.gwt.event.shared.SimpleEventBus#fireEventFromSource(com.google.web.bindery.event.shared.Event, java.lang.Object)
	 */
	@Override
	public void fireEventFromSource( Event< ? > event, Object source ) {
		if( event.getAssociatedType( ) == RegisterViewEvent.TYPE ) {
			if( !_sourceIds.contains( source ) ) {
				_sourceIds.add( source );
				RegisterViewEvent registerEvent = ( RegisterViewEvent )event;
				DataPresenter p = ( DataPresenter )PresenterFactory.create( 
					DataPresenter.class, 
					registerEvent.getView( ).getID( )
				);
				p.setRequest( registerEvent.getView( ).onInit( ) );
			}
		}
		super.fireEventFromSource( event, source );
	}
}
