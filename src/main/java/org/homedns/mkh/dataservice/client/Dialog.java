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

import org.homedns.mkh.dataservice.shared.Response;

/**
 * Dialog interface
 *
 */
public interface Dialog {

	/**
	 * Returns data to save as string (json, xml or with delimiter)
	 * 
	 * @return json or xml data string
	 */
	public String getSavingData( );

	/**
	 * Refreshes dialog
	 * 
	 * @param data the data to refresh
	 */
	public void refresh( Response data );

	/**
	 * Shows the dialog
	 */
	public void show( );
	
	/**
	 * Hides the dialog
	 */
	public void hide( );
}
