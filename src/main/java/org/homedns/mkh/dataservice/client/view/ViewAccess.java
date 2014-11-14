/*
* Copyright 2012-2014 Mikhail Khodonov
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

/**
 * View access interface
 * 
 */
public interface ViewAccess {
	/**
	 * Available access rights
	 */
	public enum Access { NO_ACCESS, READ_ONLY, READ_WRITE };
	
	/**
	 * Returns view access
	 * 
	 * @return the view access
	 */
	public Access getAccess( );
	
	/**
	 * Sets view access and send request to the
	 * {@link org.homedns.mkh.dataservice.client.presenter.AccessPresenter} to get access rights
	 * 
	 * @param access
	 *            the view access to set
	 */
	public void setAccess( Access access );
	
	/**
	 * Returns unique tag
	 * 
	 * @return the tag
	 */
	public String getTag( );
	
	/**
	 * Sets unique tag
	 * 
	 * @param sTag
	 *            the tag to set
	 */
	public void setTag( String sTag );
}
