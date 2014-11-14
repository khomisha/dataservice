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

/**
 * Create, update, delete request
 *
 */
public abstract class CUDRequest extends GenericRequest {
	private static final long serialVersionUID = -8114059419018956195L;

	private String _sData;

	public CUDRequest( ) {
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.GenericRequest#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		Request r = super.copy( inputRequest );
		if( !( r instanceof CUDRequest ) ) {
			throw new IllegalArgumentException( inputRequest.getClass( ).getName( ) );
		}
		CUDRequest request = ( CUDRequest )r;
		request.setData( _sData );
		return( request );
	}

	/**
	 * Returns saving data
	 * 
	 * @return the saving data
	 */
	public String getData( ) {
		return( _sData );
	}

	/**
	 * Sets saving data
	 * 
	 * @param sData
	 *            the data to set
	 */
	public void setData( String sData ) {
		_sData = sData;
	}
}
