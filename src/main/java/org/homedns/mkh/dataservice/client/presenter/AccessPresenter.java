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

package org.homedns.mkh.dataservice.client.presenter;

import java.util.HashMap;
import java.util.Map;
import org.homedns.mkh.dataservice.client.RPCCall;
import org.homedns.mkh.dataservice.client.event.ChangePasswordEvent;
import org.homedns.mkh.dataservice.client.event.ChangePasswordEvent.ChangePasswordHandler;
import org.homedns.mkh.dataservice.client.event.EventBus;
import org.homedns.mkh.dataservice.client.event.LoginEvent;
import org.homedns.mkh.dataservice.client.event.LoginEvent.LoginHandler;
import org.homedns.mkh.dataservice.client.event.LogoutEvent;
import org.homedns.mkh.dataservice.client.event.LogoutEvent.LogoutHandler;
import org.homedns.mkh.dataservice.client.event.SetAccessEvent;
import org.homedns.mkh.dataservice.client.event.SetAccessEvent.SetAccessHandler;
import org.homedns.mkh.dataservice.client.view.ViewAccess;
import org.homedns.mkh.dataservice.client.view.ViewAccess.Access;
import org.homedns.mkh.dataservice.shared.Response;

/**
 * Access presenter decides whether an access to a application resource is to be
 * allowed or denied
 * 
 */
public abstract class AccessPresenter extends Presenter implements LoginHandler, LogoutHandler, SetAccessHandler, ChangePasswordHandler {
	private Map< String, Integer > _widgetsAccess = new HashMap< String, Integer >( );
	
	public AccessPresenter( ) {
		super( );
		addHandler( EventBus.getInstance( ).addHandler( LoginEvent.TYPE, this ) );
		addHandler( EventBus.getInstance( ).addHandler( LogoutEvent.TYPE, this ) );
		addHandler( EventBus.getInstance( ).addHandler( SetAccessEvent.TYPE, this ) );
		addHandler( EventBus.getInstance( ).addHandler( ChangePasswordEvent.TYPE, this ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.LogoutEvent.LogoutHandler#onLogout(org.homedns.mkh.dataservice.client.event.LogoutEvent)
	 */
	@Override
	public void onLogout( LogoutEvent event ) {
		setRequest( event.getRequest( ) );
		RPCCall rpc = new RPCCall( );
		rpc.execute( this );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.LoginEvent.LoginHandler#onLogin(org.homedns.mkh.dataservice.client.event.LoginEvent)
	 */
	@Override
	public abstract void onLogin( LoginEvent event );

	/**
	 * @see org.homedns.mkh.dataservice.client.event.ChangePasswordEvent.ChangePasswordHandler#onChangePassword(org.homedns.mkh.dataservice.client.event.ChangePasswordEvent)
	 */
	@Override
	public abstract void onChangePassword( ChangePasswordEvent event );
	
	/**
	 * @see org.homedns.mkh.dataservice.client.presenter.Presenter#onResponse(org.homedns.mkh.dataservice.shared.Response)
	 */
	@Override
	public abstract void onResponse( Response result );

	/**
	 * @see org.homedns.mkh.dataservice.client.event.SetAccessEvent.SetAccessHandler#onSetAccess(org.homedns.mkh.dataservice.client.event.SetAccessEvent)
	 */
	@Override
	public void onSetAccess( SetAccessEvent event ) {
		setAccess( event.getWidget( ) );
	}

	/**
	 * Sets widget access.
	 * 
	 * @param widget
	 *            the widget access rights applies to
	 */
	protected void setAccess( ViewAccess widget ) {
		Integer iAccess = _widgetsAccess.get( widget.getTag( ) );
		if( iAccess == null ) {
			// access rights are not set, using widget as is i.e. READ_WRITE 
			widget.setAccess( Access.READ_WRITE );
		} else {
			widget.setAccess( Access.values( )[ iAccess ] );
		}
	}

	/**
	 * Loads widgets access definition from database
	 * 
	 * @param response
	 *            the response
	 */
	protected abstract void loadAccess( Response response );
	
	/**
	 * Returns widgets access map
	 * 
	 * @return the widgets access map
	 */
	protected Map< String, Integer > getWidgetAccess( ) {
		return( _widgetsAccess );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.presenter.Presenter#init()
	 */
	@Override
	protected void init( ) {
	}
}
