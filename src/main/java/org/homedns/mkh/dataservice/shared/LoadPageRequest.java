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
 * Load page request
 *
 */
public class LoadPageRequest extends GenericRequest implements Serializable {
	private static final long serialVersionUID = 1596717063902564958L;

	private int _iLoadingPageNum;
	
	public LoadPageRequest( ) {
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.LoadPageHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.GenericResponse" );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.GenericRequest#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		Request r = super.copy( inputRequest );
		if( !( r instanceof LoadPageRequest ) ) {
			throw new IllegalArgumentException( inputRequest.getClass( ).getName( ) );
		}
		LoadPageRequest request = ( LoadPageRequest )r;
		request.setLoadingPageNum( _iLoadingPageNum );
		return( request );
	}

	/**
	 * Returns loading page number
	 * 
	 * @return the loading page number
	 */
	public int getLoadingPageNum( ) {
		return( _iLoadingPageNum );
	}

	/**
	 * Sets loading page number
	 * 
	 * @param iLoadingPageNum
	 *            the loading page number to set
	 */
	public void setLoadingPageNum( int iLoadingPageNum ) {
		_iLoadingPageNum = iLoadingPageNum;
	}
}
