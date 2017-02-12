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

import java.util.logging.Logger;

import org.homedns.mkh.dataservice.client.Paging;
import org.homedns.mkh.dataservice.client.RPCCall;
import org.homedns.mkh.dataservice.client.event.CacheLoadedEvent;
import org.homedns.mkh.dataservice.client.event.CacheLoadedEvent.CacheLoadedHandler;
import org.homedns.mkh.dataservice.client.event.EventBus;
import org.homedns.mkh.dataservice.client.event.RPCallEvent;
import org.homedns.mkh.dataservice.client.event.RemoveViewEvent;
import org.homedns.mkh.dataservice.client.event.RPCallEvent.RPCallHandler;
import org.homedns.mkh.dataservice.client.event.RegisterViewEvent;
import org.homedns.mkh.dataservice.client.event.RegisterViewEvent.RegisterViewHandler;
import org.homedns.mkh.dataservice.client.event.RemoveViewEvent.RemoveWidgetHandler;
import org.homedns.mkh.dataservice.client.view.View;
import org.homedns.mkh.dataservice.shared.ClosePagingConnRequest;
import org.homedns.mkh.dataservice.shared.DeleteResponse;
import org.homedns.mkh.dataservice.shared.InsertResponse;
import org.homedns.mkh.dataservice.shared.RemoveDataBufferResponse;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.RequestFactory;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Id;
import org.homedns.mkh.dataservice.shared.RemoveDataBufferRequest;
import org.homedns.mkh.dataservice.shared.RetrieveResponse;
import org.homedns.mkh.dataservice.shared.UpdateResponse;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

/**
 * Data presenter presents data handle for view implementations
 *
 */
public class DataPresenter extends Presenter implements RPCallHandler, RegisterViewHandler, RemoveWidgetHandler, CacheLoadedHandler {
	private static final Logger LOG = Logger.getLogger( DataPresenter.class.getName( ) );  
	
	public DataPresenter( ) {
		super( );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.client.event.RPCallEvent.RPCallHandler#onRPCall(org.homedns.mkh.dataservice.client.event.RPCallEvent)
	 */
	@Override
	public void onRPCall( RPCallEvent event ) {
		execRPC( event.getRequest( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.presenter.Presenter#onResponse(org.homedns.mkh.dataservice.shared.Response)
	 */
	@Override
	public void onResponse( Response response ) {
		if( response instanceof RemoveDataBufferResponse ) { 
			removeHandlers( );
			return;
		} 
		super.onResponse( response );
		setResponse( response );
		getViewCache( ).setData( response );
		if( 
			response instanceof InsertResponse || 
			response instanceof DeleteResponse || 
			response instanceof UpdateResponse ||
			response instanceof RetrieveResponse
		) {
			for( View view : getViewList( ) ) {
				LOG.config( "onResponse: " + getID( ).toString( ) + ": calls onResponse: " + view.getID( ).toString( ) );
				view.onResponse( response );
			}
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.CacheLoadedEvent.CacheLoadedHandler#onCacheLoaded(org.homedns.mkh.dataservice.client.event.CacheLoadedEvent)
	 */
	@Override
	public void onCacheLoaded( CacheLoadedEvent event ) {
		View newView = getInitiatingView( );
		if( newView != null ) {
			newView.setCache( getViewCache( ) );
			LOG.config( "onCacheLoaded: " + getID( ).toString( ) + ": calls init view: " + newView.getID( ).toString( ) );
			newView.init( getDescription( ) );
			setInitiatingView( null );
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.event.RegisterViewEvent.RegisterViewHandler#onRegister(org.homedns.mkh.dataservice.client.event.RegisterViewEvent)
	 */
	@Override
	public void onRegister( RegisterViewEvent event ) {
		final View view = event.getView( );
		LOG.config( getID( ).toString( ) + ": catch register event from view: " + view.getID( ).toString( ) + ": " + view.getClass( ).getName( ) );
		if( !add( view ) ) {
			return;
		}
		if( isInit( ) ) {
			// presenter and view cache were initialized therefore can init view and
			// shouldn't need send request to the server 
			LOG.config( "onRegister: " + getID( ).toString( ) + ": calls init view: " + view.getID( ).toString( ) + ": " + view.getClass( ).getName( ) );
			initView( view );
		} else {
			if( isRegisterLock( ) ) {
			    Scheduler.get( ).scheduleIncremental( 
			    	new RepeatingCommand( ) {
			    		@Override
			    		public boolean execute( ) {
			    			boolean bAgain = true;
			    			if( !isRegisterLock( ) ) {
			    				if( isInit( ) ) {
				    				LOG.config( "onRegister: RepeatingCommand: " + getID( ).toString( ) + ": calls initView(): " + view.getID( ).toString( ) + ": " + view.getClass( ).getName( ) );
			    					initView( view );
				    				bAgain = false;
			    				}
			    			}
			    			return( bAgain );
			    		}
			    	}
			    );	
			} else {
				LOG.config( 
					"onRegister: no lock:" + 
					getID( ).toString( ) + 
					": calls dataRequest(): " + view.getID( ).toString( ) + ": " + view.getClass( ).getName( )
				);
				dataRequest( view );
			}
		}
	}
	
	/**
	 * Requests data first time on view register event
	 * 
	 * @param view the view which requests data
	 */
	private void dataRequest( View view ) {
		// presenter is created when EventBus intercepts RegisterViewEvent
		setRegisterLock( true );
		setInitiatingView( view );
		getRequest( ).setInitPresenter( false );
		execRPC( getRequest( ) );						
	}
	
	/**
	 * Inits view
	 * 
	 * @param view the view to init
	 */
	private void initView( View view ) {
		// presenter and view cache were initialized therefore can init view and
		// shouldn't need send request to the server 
		view.setCache( getViewCache( ) );
		view.init( getDescription( ) );
		view.onResponse( getViewCache( ).getData( ) );				
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.client.event.RemoveViewEvent.RemoveWidgetHandler#onRemove(org.homedns.mkh.dataservice.client.event.RemoveViewEvent)
	 */
	@Override
	public void onRemove( RemoveViewEvent event ) {
		remove( event.getView( ) );	
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.presenter.Presenter#remove(org.homedns.mkh.dataservice.client.view.View)
	 */
	@Override
	protected void remove( View view ) {
		super.remove( view );
		if( getViewList( ).isEmpty( ) ) {
			execRPC( RequestFactory.create( RemoveDataBufferRequest.class ) );
		} else {
			if( view instanceof Paging ) {
				Paging paging = ( Paging )view;
				if( paging.isServerPaging( ) ) {
					execRPC( RequestFactory.create( ClosePagingConnRequest.class ) );
				}
			}			
		}
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.client.presenter.Presenter#init()
	 */
	@Override
	protected void init( ) {
		Id id = getID( );
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( RPCallEvent.TYPE, id.getUID( ), this ) 
		);
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( RegisterViewEvent.TYPE, id.getUID( ), this ) 
		);
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( RemoveViewEvent.TYPE, id.getUID( ), this )
		);
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( CacheLoadedEvent.TYPE, id.getUID( ), this )
		);
 	}
	
	/**
	 * Executes RPC
	 * 
	 * @param request
	 *            the request
	 */
	protected void execRPC( Request request ) {
		request.setID( getID( ) );
		setRequest( request );
		RPCCall rpc = new RPCCall( );
		rpc.execute( this );					
	}
}