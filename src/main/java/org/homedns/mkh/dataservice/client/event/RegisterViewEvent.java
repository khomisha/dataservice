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
 * Register view event
 *
 */
public class RegisterViewEvent extends GenericEvent< RegisterViewEvent.RegisterViewHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface RegisterViewHandler extends EventHandler {
		public void onRegister( RegisterViewEvent event );
	}
	  
	private View _view;
	public static final Event.Type< RegisterViewHandler > TYPE = new Event.Type< RegisterViewHandler >( );

	/**
	 * Fires register view event
	 * 
	 * @param view the view to register
	 */
	public static void fire( View view ) {
		RegisterViewEvent event = new RegisterViewEvent( );
		event.setView( view );
		fire( event, view.getID( ).hashCode( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< RegisterViewHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( RegisterViewHandler handler ) {
		handler.onRegister( this );
	}

	/**
	 * Returns register view
	 * 
	 * @return the view
	 */
	public View getView( ) {
		return( _view );
	}

	/**
	 * Sets view to register
	 * 
	 * @param view the view to set
	 */
	public void setView( View view ) {
		_view = view;
	}
}
