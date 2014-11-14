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

package org.homedns.mkh.dataservice.client.view;

import org.homedns.mkh.dataservice.client.Creator;
import org.homedns.mkh.dataservice.shared.Id;
import com.google.gwt.core.shared.GWT;

/**
 * View cache factory
 *
 */
public class ViewCacheFactory {

	/**
	 * View cache creator
	 */
	public interface ViewCacheCreator extends Creator< ViewCache > {
	}
	
	private static ViewCacheCreator _creator;

	/**
	 * Creates view cache
	 * 
	 * 
	 * @param type
	 *            the view cache class type
	 * @param desc
	 *            the data buffer description object
	 * @param id
	 *            the identification object
	 *            
	 * @return the view cache
	 */
	public static ViewCache create( Class< ? > type, ViewDesc desc, Id id ) {
		if( _creator == null ) {
			_creator = GWT.create( ViewCacheCreator.class );
		}
		ViewCache cache = _creator.instantiate( type );
		cache.setID( id );
		cache.setViewDesc( desc );
		return( cache );
	}
}
