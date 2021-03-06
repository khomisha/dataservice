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

import org.homedns.mkh.dataservice.client.Dialog;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Change password event
 *
 */
public class ChangePasswordEvent extends GenericEvent< ChangePasswordEvent.ChangePasswordHandler > {
			
	/**
	 * Implemented by objects that handle.
	 */
	public interface ChangePasswordHandler extends EventHandler {
		public void onChangePassword( ChangePasswordEvent event );
	}
	  
	private Dialog _dialog;
	public static final Event.Type< ChangePasswordHandler > TYPE = new Event.Type< ChangePasswordHandler >( );

	/**
	 * Fires change password event
	 * 
	 * @param dialog
	 *            the source event data
	 */
	public static void fire( Dialog dialog ) {
		ChangePasswordEvent event = new ChangePasswordEvent( );
		event.setDialog( dialog );
		fire( event, null );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#getType()
	 */
	@Override
	protected Event.Type< ChangePasswordHandler > getType( ) {
		return( TYPE );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.GenericEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch( ChangePasswordHandler handler ) {
		handler.onChangePassword( this );
	}

	/**
	 * Returns change password dialog
	 * 
	 * @return the dialog
	 */
	public Dialog getDialog( ) {
		return( _dialog );
	}

	/**
	 * Sets change password dialog
	 * 
	 * @param dialog
	 *            the dialog to set
	 */
	public void setDialog( Dialog dialog ) {
		_dialog = dialog;
	}
}
