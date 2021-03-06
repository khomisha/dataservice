/*
 * Copyright 2015 Mikhail Khodonov
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


/**
 * Retrieve response
 *
 */
public class RetrieveResponse extends GenericResponse {
	private static final long serialVersionUID = 4290046176611769303L;

	private boolean bForcedRetrieve = false;

	public RetrieveResponse( ) {
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.view.View#isForcedRetrieve()
	 */
	public boolean isForcedRetrieve( ) {
		return( bForcedRetrieve );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.view.View#setForcedRetrieve(boolean)
	 */
	public void setForcedRetrieve( boolean bForcedRetrieve ) {
		this.bForcedRetrieve = bForcedRetrieve;
	}
}
