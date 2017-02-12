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

import org.homedns.mkh.dataservice.client.view.View;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Remove view event
 *
 */
public class RemoveViewEvent extends GenericEvent< RemoveViewEvent.RemoveWidgetHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface RemoveWidgetHandler extends EventHandler {
		public void onRemove( RemoveViewEvent event );
	}
	  
	private View _view;
	public static final Event.Type< RemoveWidgetHandler > TYPE = new Event.Type< RemoveWidgetHandler >( );

	/**
	 * Fires remove view event
	 * 
	 * @param view
	 *            the source view
	 */
	public static void fire( View view ) {
		RemoveViewEvent event = new RemoveViewEvent( );
		event.setView( view );
		fire( event, view.getID( ).getUID( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< RemoveWidgetHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( RemoveWidgetHandler handler ) {
		handler.onRemove( this );
	}

	/**
	 * Returns view
	 * 
	 * @return the view
	 */
	public View getView( ) {
		return( _view );
	}

	/**
	 * Sets view
	 * 
	 * @param view
	 *            the view to set
	 */
	public void setView( View view ) {
		_view = view;
	}
}
