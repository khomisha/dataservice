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

package org.homedns.mkh.dataservice.client.sender;

import org.homedns.mkh.dataservice.client.view.View;
import org.homedns.mkh.dataservice.shared.Request;

/**
 * The request {@link org.homedns.mkh.dataservice.shared.Request} sender
 *
 */
public interface RequestSender {
	
	/**
	 * Returns request
	 * 
	 * @return the request
	 */
	public Request getRequest( );
	
	/**
	 * Sets sending request
	 * 
	 * @param request
	 *            the request to set
	 */
	public void setRequest( Request request );
	
	/**
	 * Sends previously bound request
	 */
	public void send( );
	
	/**
	 * Sets view as a sending request source
	 * 
	 * @param view
	 *            the view to set
	 */
	public void setView( View view );
}
