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

package org.homedns.mkh.dataservice.client;

import org.homedns.mkh.dataservice.client.view.View;

/**
 * Paging (providing automatic paging controls) widget interface
 *
 */
public interface Paging extends View {
		
	/**
	 * Returns page object
	 * 
	 * @return the page object
	 */
	public Page getPage( );
	
	/**
	 * Returns widget page size. Page size is set in data buffer description.
	 * Page size = 0 means paging off
	 * 
	 * @return the widget page size
	 */
	public int getPageSize( );

	/**
	 * Returns true if server paging on and false otherwise
	 * 
	 * @return the server paging flag
	 */
	public boolean isServerPaging( );
}
