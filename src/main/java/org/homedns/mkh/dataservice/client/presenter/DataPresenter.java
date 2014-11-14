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
import org.homedns.mkh.dataservice.shared.ReportResponse;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.RequestFactory;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Id;
import org.homedns.mkh.dataservice.shared.RemoveDataBufferRequest;
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
		} else if( response instanceof ReportResponse ) {
			processReport( response.getDownloadFileName( ) );
			return;
		} 
		super.onResponse( response );
		setResponse( response );
		getViewCache( ).setData( response );
		if( 
			response instanceof InsertResponse || 
			response instanceof DeleteResponse || 
			response instanceof UpdateResponse 
		) {
			for( View view : getViewList( ) ) {
				view.refresh( response );
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
		LOG.config( "catch register event from view: " + view.getID( ).toString( ) );
		if( !add( view ) ) {
			return;
		}
		if( isRegisterLock( ) ) {
		    Scheduler.get( ).scheduleIncremental( 
		    	new RepeatingCommand( ) {
		    		@Override
		    		public boolean execute( ) {
		    			boolean bAgain = true;
		    			if( !isRegisterLock( ) ) {
		    				initView( view );
		    				bAgain = false;
		    			}
		    			return( bAgain );
		    		}
		    	}
		    );			
		} else {
			if( isInit( ) ) {
				initView( view );
			} else {
				// presenter is created when EventBus intercepts RegisterViewEvent
				setRegisterLock( true );
				setInitiatingView( view );
				getRequest( ).setInitPresenter( false );
				execRPC( getRequest( ) );				
			}
		}
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
	 * Process report
	 * 
	 * @param sFile
	 *            the report file name
	 */
	protected void processReport( String sFile ) {}
	
	/**
	 * @see org.homedns.mkh.dataservice.client.presenter.Presenter#init()
	 */
	@Override
	protected void init( ) {
		Id id = getID( );
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( RPCallEvent.TYPE, id.hashCode( ), this ) 
		);
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( RegisterViewEvent.TYPE, id.hashCode( ), this ) 
		);
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( RemoveViewEvent.TYPE, id.hashCode( ), this )
		);
		addHandler( 
			EventBus.getInstance( ).addHandlerToSource( CacheLoadedEvent.TYPE, id.hashCode( ), this )
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

	/**
	 * Inits view when presenter is already initialized
	 * 
	 * @param view
	 *            the view to initialize
	 */
	private void initView( View view ) {
		// shouldn't need send request to the server
		view.setCache( getViewCache( ) );
		view.init( getDescription( ) );
		view.refresh( getViewCache( ).getData( ) );		
	}
}
