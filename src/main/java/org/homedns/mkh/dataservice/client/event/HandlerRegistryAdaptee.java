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
import java.util.List;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Events handler registration list
 *
 */
public class HandlerRegistryAdaptee {
	List< HandlerRegistration > _list = new ArrayList< HandlerRegistration >( );

	/**
	 * Adds handler registration object to the list
	 * 
	 * @param hr
	 *            the handler registration object to add
	 */
	public boolean add( HandlerRegistration hr ) {
		return( _list.add( hr ) );
	}
	
	/**
	 * Removes all handlers from this list
	 */
	public void clear( ) {
		for( HandlerRegistration hr : _list ) {
			hr.removeHandler( );
		}
		_list.clear( );
	}
}
