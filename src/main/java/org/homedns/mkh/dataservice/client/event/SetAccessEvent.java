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

import org.homedns.mkh.dataservice.client.view.ViewAccess;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Set access rights for UI object event
 *
 */
public class SetAccessEvent extends GenericEvent< SetAccessEvent.SetAccessHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface SetAccessHandler extends EventHandler {
		public void onSetAccess( SetAccessEvent event );
	}
	  
	private ViewAccess _widget;
	public static final Event.Type< SetAccessHandler > TYPE = new Event.Type< SetAccessHandler >( );

	/**
	 * Fires set access event
	 * 
	 * @param widget
	 *            the source widget
	 */
	public static void fire( ViewAccess widget ) {
		SetAccessEvent event = new SetAccessEvent( );
		event.setWidget( widget );
		fire( event, null );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< SetAccessHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( SetAccessHandler handler ) {
		handler.onSetAccess( this );
	}

	/**
	 * Returns widget
	 * 
	 * @return the widget
	 */
	public ViewAccess getWidget( ) {
		return( _widget );
	}

	/**
	 * Sets widget
	 * 
	 * @param widget
	 *            the widget to set
	 */
	public void setWidget( ViewAccess widget ) {
		_widget = widget;
	}
}
