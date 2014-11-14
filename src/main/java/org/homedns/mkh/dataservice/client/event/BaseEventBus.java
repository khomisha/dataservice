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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.UmbrellaException;

/**
 * Basic implementation of {@link com.google.web.bindery.event.shared.EventBus}.
 * @see <a href="http://code.google.com/p/google-web-toolkit/issues/detail?id=7010">issue 7010</a>
 * @see <a href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3661">issue 3661</a>
 * @see <a href="https://groups.google.com/forum/#!topic/google-web-toolkit/drvbCuajDEg">SimpleEventBus: How to add a handler and fire an event for it when nested in a "parent" handler?</a>
 */
public class BaseEventBus extends EventBus {

	/**
	 * Map of event type to map of event source to list of their handlers.
	 */
	private final Map< Event.Type< ? >, Map< Object, List< ? > > > _map =
		new HashMap< Event.Type< ? >, Map< Object, List< ? > > >( );

	/**
	 * @see com.google.web.bindery.event.shared.EventBus#addHandler(com.google.web.bindery.event.shared.Event.Type, java.lang.Object)
	 */
	@Override
	public < H > HandlerRegistration addHandler( Type< H > type, H handler ) {
	    return doAdd( type, null, handler );
	}

	/**
	 * @see com.google.web.bindery.event.shared.EventBus#addHandlerToSource(com.google.web.bindery.event.shared.Event.Type, java.lang.Object, java.lang.Object)
	 */
	@Override
	public < H > HandlerRegistration addHandlerToSource( Type< H > type, Object source, H handler ) {
	    if( source == null ) {
	    	throw new NullPointerException( "Cannot add a handler with a null source" );
        }
	    return doAdd( type, source, handler );
	}

	/**
	 * @see com.google.web.bindery.event.shared.EventBus#fireEvent(com.google.web.bindery.event.shared.Event)
	 */
	@Override
	public void fireEvent( Event< ? > event ) {
	    doFire( event, null );
	}

	/**
	 * @see com.google.web.bindery.event.shared.EventBus#fireEventFromSource(com.google.web.bindery.event.shared.Event, java.lang.Object)
	 */
	@Override
	public void fireEventFromSource( Event< ? > event, Object source ) {
	    if( source == null ) {
	    	throw new NullPointerException( "Cannot fire from a null source" );
	    }
	    doFire( event, source );
	}

	/**
	 * Removes handler
	 * 
	 * @param type
	 *            the event type
	 * @param source
	 *            the event source
	 * @param handler
	 *            the event handler
	 */
	protected < H > void doRemove( 
		Event.Type< H > type, 
		Object source, 
		H handler 
	) {
		List< H > handlers = getHandlerList( type, source );
		boolean removed = handlers.remove( handler );
		if( removed && handlers.isEmpty( ) ) {
			Map< Object, List< ? > > sourceMap = _map.get( type );
			sourceMap.remove( source );
			if( sourceMap.isEmpty( ) ) {
				_map.remove( type );
			}
		}
	}

	/**
	 * Fires event
	 * 
	 * @param event
	 *            the event to fire
	 * @param source
	 *            the event source
	 */
	private < H > void doFire( Event< H > event, Object source ) {
		if( event == null ) {
			throw new NullPointerException( "Cannot fire null event" );
		}
		if( source != null ) {
			setSourceOfEvent( event, source );
		}

		List< H > handlers = getDispatchList( event.getAssociatedType( ), source );
		Set< Throwable > causes = null;
		for( H handler : handlers ) {
			try {
				dispatchEvent( event, handler );
			} 
			catch( Throwable e ) {
				if( causes == null ) {
					causes = new HashSet< Throwable >( );
				}
				causes.add( e );
			}
		}
		if( causes != null ) {
			throw new UmbrellaException( causes );
		}
	}
	
	/**
	 * Returns handlers dispatch list for specified event type and event source
	 * 
	 * @param type
	 *            the event type
	 * @param source
	 *            the event source
	 * 
	 * @return the event handlers list
	 */
	private < H > List< H > getDispatchList( Event.Type< H > type, Object source ) {
		List< H > globalHandlers = getHandlerList( type, null );
		List< H > totalList = new ArrayList< H >( globalHandlers );
		if( source != null ) {
			List< H > handlers = getHandlerList( type, source );
			totalList.addAll( handlers );
		}
		return( totalList );
	}

	/**
	 * Returns handlers list for specified event type and event source
	 * 
	 * @param type
	 *            the event type
	 * @param source
	 *            the event source
	 * 
	 * @return the event handlers list
	 */
	@SuppressWarnings( "unchecked" )
	private < H > List< H > getHandlerList( Event.Type< H > type, Object source ) {
		List< H > handlers = Collections.emptyList( );
		Map< Object, List< ? > > sourceMap = _map.get( type );
		if( sourceMap != null ) {
			handlers = ( List< H > )sourceMap.get( source );
			if( handlers == null ) {
				handlers = Collections.emptyList( );
			}
		}
		return( handlers );
	}

	/**
	 * Adds event handler
	 * 
	 * @param type
	 *            the event type
	 * @param source
	 *            the event source
	 * @param handler
	 *            the event handler to add
	 * 
	 * @return the handler registration object
	 */
	private < H > HandlerRegistration doAdd( 
		final Event.Type< H > type, 
		final Object source, 
		final H handler 
	) {
		if( type == null ) {
			throw new NullPointerException( "Cannot add a handler with a null type" );
		}
		if( handler == null ) {
			throw new NullPointerException( "Cannot add a null handler" );
		}
	
	    Map< Object, List< ? > > sourceMap = _map.get( type );
	    if( sourceMap == null ) {
	    	sourceMap = new HashMap< Object, List< ? > >( );
	    	_map.put( type, sourceMap );
	    }
	    @SuppressWarnings("unchecked")
	    List< H > handlers = ( List< H > )sourceMap.get( source );
	    if( handlers == null ) {
	      handlers = new ArrayList< H >( );
	      sourceMap.put( source, handlers );
	    }
	    handlers.add( handler );
	    
	    return(
    		new HandlerRegistration( ) {
    			public void removeHandler( ) {
    				doRemove( type, source, handler );
    			}
    		}
    	);
	}
}
