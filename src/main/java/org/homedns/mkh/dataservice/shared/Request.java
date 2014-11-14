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

package org.homedns.mkh.dataservice.shared;

import java.io.Serializable;

/**
 * Request interface
 *
 */
public interface Request extends Serializable {
	
	/**
	 * Copies this request attributes values to the specified request
	 * 
	 * @param request the destination request
	 * 
	 * @return the destination request
	 */
	public Request copy( Request request );
	
	/**
	 * Sets unique identification object of impact object.
	 * 
	 * @param id
	 *            the identification object to set
	 */
	public void setID( Id id );

	/**
	 * Returns impact object unique identification object
	 * 
	 * @return the identification object
	 */
	public Id getID( );
	
	/**
	 * Returns request handler class name
	 * 
	 * @return the request handler class name
	 */
	public String getHandlerClassName( );

	/**
	 * Sets request handler class name
	 * 
	 * @param sHandlerClassName
	 *            the request handler class name to set
	 */
	public void setHandlerClassName( String sHandlerClassName );

	/**
	 * Returns response class name
	 * 
	 * @return the response class name
	 */
	public String getResponseClassName( );

	/**
	 * Sets response class name
	 * 
	 * @param sResponseClassName
	 *            the response class name to set
	 */
	public void setResponseClassName( String sResponseClassName );

	/**
	 * Returns init presenter flag
	 * 
	 * @return the init presenter flag
	 */
	public boolean isInitPresenter( );

	/**
	 * Sets init presenter flag
	 * 
	 * @param bInitPresenter
	 *            the init presenter flag to set
	 */
	public void setInitPresenter( boolean bInitPresenter );
	
	/**
	 * Returns request sender
	 * {@link org.homedns.mkh.dataservice.client.sender.RequestSender} class
	 * name
	 * 
	 * @return the request sender class name
	 */
	public String getSenderType( );
	
	/**
	 * Sets request sender
	 * {@link org.homedns.mkh.dataservice.client.sender.RequestSender} class
	 * name
	 * 
	 * @param sClassName
	 *            the request sender class name to set
	 */
	public void setSenderType( String sClassName );
}
