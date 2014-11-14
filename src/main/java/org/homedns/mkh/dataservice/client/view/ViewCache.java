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

package org.homedns.mkh.dataservice.client.view;

import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Id;

/**
 * View cache
 *
 */
public interface ViewCache {

	/**
	 * Returns data
	 * 
	 * @return the view cache data
	 */
	public Response getData( );
	
	/**
	 * Returns view identification object
	 * 
	 * @return the view identification object
	 */
	public Id getID( );
	
	/**
	 * Sets data
	 * 
	 * @param data
	 *            the data to set
	 */
	public void setData( Response data );
	
	/**
	 * Sets view identification object to uniquely identify bound data presenter.
	 * 
	 * @param id
	 *            the view identification object to set
	 */
	public void setID( Id id );
	
	/**
	 * Returns view cache row count
	 * 
	 * @return the widget row count
	 */
	public int getRowCount( );
	
	/**
	 * Returns view description
	 * 
	 * @return the view description
	 */
	public ViewDesc getViewDesc( );
	
	/**
	 * Sets view description and inits view cache implementation specific
	 * objects
	 * 
	 * @param desc
	 *            the view description to set
	 */
	public void setViewDesc( ViewDesc desc );
}
