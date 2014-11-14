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

import org.homedns.mkh.dataservice.client.DataBufferDesc;

/**
 * The inner widget description interface. It is derived from data buffer
 * description and depending on the particular widget implementation
 * 
 */
public interface ViewDesc {

	/**
	 * Returns data buffer description object
	 * 
	 * @return the data buffer description object
	 */
	public DataBufferDesc getDataBufferDesc( );
	
	/**
	 * Sets data buffer description and inits widget implementation specific
	 * objects
	 * 
	 * @param desc
	 *            the data buffer description
	 */
	public void setDataBufferDesc( DataBufferDesc desc );
}
