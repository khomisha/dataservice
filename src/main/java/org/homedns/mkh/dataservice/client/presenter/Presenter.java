/*
 * Copyright 2013-2018 Mikhail Khodonov
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.homedns.mkh.dataservice.client.DataBufferDesc;
import org.homedns.mkh.dataservice.client.event.HandlerRegistryAdaptee;
import org.homedns.mkh.dataservice.client.event.HandlerRegistry;
import org.homedns.mkh.dataservice.client.view.View;
import org.homedns.mkh.dataservice.client.view.ViewCache;
import org.homedns.mkh.dataservice.client.view.ViewCacheFactory;
import org.homedns.mkh.dataservice.client.view.ViewDesc;
import org.homedns.mkh.dataservice.client.view.ViewDescFactory;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Id;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Base presenter
 * 
 */
public abstract class Presenter implements HandlerRegistry {
	private static final Logger LOG = Logger.getLogger( Presenter.class.getName( ) );  

	private Response response;
	private Id id;
	private List< View > viewList = new ArrayList< View >( );
	private ViewDesc viewDesc;
	private ViewCache viewCache;
	private HandlerRegistryAdaptee handlers;
	private Request request;
	private View newView;
	private boolean bRegisterLock = false;
	private static boolean bLogoutInProcess = false;
	
	public Presenter( ) {
		handlers = new HandlerRegistryAdaptee( );
	}
	
	/**
	 * Sets unique identification object and inits presenter
	 * implementation specific objects
	 * 
	 * @param id
	 *            the presenter identification object to set
	 */
	public void setID( Id id ) {
		this.id = id;
		init( );
	}

	/**
	 * Returns unique identification object
	 * 
	 * @return the identification object
	 */
	public Id getID( ) {
		return( id );
	}

	/**
	 * Returns request
	 * 
	 * @return the request
	 */
	public Request getRequest( ) {
		return( request );
	}

	/**
	 * Sets request
	 * 
	 * @param request
	 *            the request to set
	 */
	public void setRequest( Request request ) {
		this.request = request;
	}

	/**
	 * Sets response data
	 * 
	 * @param response
	 *            the response data to set
	 */
	public void setResponse( Response response ) {
		this.response = response;
		this.response.setID( id );		//????
	}

	/**
	 * Returns response data
	 * 
	 * @return the response data
	 */
	public Response getResponse( ) {
		return( response );
	}

	/**
	 * Returns true if presenter is initialized and false otherwise
	 * 
	 * @return the presenter init flag
	 */
	public boolean isInit( ) {
		return( viewDesc != null );
	}

	/**
	 * Presenter reaction at RPC call response, typically should't directly call
	 * 
	 * @param response
	 *            the response
	 */
	public void onResponse( Response response ) {
		if( !isInit( ) ) {
			viewDesc = ViewDescFactory.create( 
				ViewDesc.class, getDataBufferDesc( response ) 
			);
			viewCache = ViewCacheFactory.create( 
				newView.getCacheType( ), 
				viewDesc, 
				id 
			);
		}
	}

	/**
	 * Returns list of views
	 * 
	 * @return the list of views
	 */
	public List< View > getViewList( ) {
		return( viewList );
	}

	/**
	 * Returns view description
	 * 
	 * @return the view description
	 */
	public ViewDesc getViewDesc( ) {
		return( viewDesc );
	}

	/**
	 * Returns view cache
	 * 
	 * @return the view cache
	 */
	public ViewCache getViewCache( ) {
		return( viewCache );
	}

	/**
	 * Adds view to the this presenter
	 * 
	 * @param view
	 *            the view to add
	 *            
	 * @return true if view added and false otherwise
	 */
	protected boolean add( View view ) {
		boolean bAdd = false;
		if( !viewList.contains( view ) ) {
			bAdd = viewList.add( view );
		}
		return( bAdd );
	}
	
	/**
	 * Returns view description
	 * 
	 * @return the view description
	 */
	public ViewDesc getDescription( ) {
		return( viewDesc );
	}

	/**
	 * Removes view from this presenter
	 * 
	 * @param view
	 *            the view to remove
	 */
	protected void remove( View view ) {
		if( viewList.contains( view ) ) {
			viewList.remove( view );
		}		
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.HandlerRegistry#removeHandlers()
	 */
	@Override
	public void removeHandlers( ) {
		handlers.clear( );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.HandlerRegistry#addHandler(com.google.web.bindery.event.shared.HandlerRegistration)
	 */
	@Override
	public boolean addHandler( HandlerRegistration hr ) {
		return( handlers.add( hr ) );		
	}

	/**
	 * Returns view register process locking flag
	 * 
	 * @return the view register process locking flag
	 */
	public boolean isRegisterLock( ) {
		return( bRegisterLock );
	}

	/**
	 * Sets the locking flag. If TRUE it's lock register requests from views bounded
	 * to the presenter until current register process is completed
	 * 
	 * @param bRegisterLock
	 *            the view register process locking flag to set
	 */
	public void setRegisterLock( boolean bRegisterLock ) {
		this.bRegisterLock = bRegisterLock;
	}

	/**
	 * Returns initiating view
	 * 
	 * @return the initiating view
	 */
	protected View getInitiatingView( ) {
		return( newView );
	}

	/**
	 * Sets initiating view
	 * 
	 * @param newView
	 *            the initiating view to set
	 */
	protected void setInitiatingView( View newView ) {
		this.newView = newView;
	}
	
	/**
	 * Inits presenter implementation specific objects
	 */
	protected abstract void init( );
	
	/**
	 * Returns data buffer description object restored from json string
	 * 
	 * @param response
	 *            the response
	 * 
	 * @return the data buffer description object
	 */
	protected DataBufferDesc getDataBufferDesc( Response response ) {
		String sJson = response.getDataBufferDesc( );
		LOG.config( LOG.getName( ) + ": " + sJson );
		DataBufferDesc desc = DataBufferDesc.DECODER.decode( JSONParser.parseStrict( sJson ) );
		return( desc );
	}

	/**
	 * Returns logout in process flag
	 * 
	 * @return the logout in process flag
	 */
	public static boolean isLogoutInProcess( ) {
		return( bLogoutInProcess );
	}

	/**
	 * Sets logout in process flag
	 * 
	 * @param bLogoutInProcess the logout in process flag to set
	 */
	public static void setLogoutInProcess( boolean bLogoutInProcess ) {
		Presenter.bLogoutInProcess = bLogoutInProcess;
	}
}
