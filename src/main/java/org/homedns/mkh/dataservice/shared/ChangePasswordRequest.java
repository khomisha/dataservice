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
 * Change password request
 *
 */
public class ChangePasswordRequest extends GenericRequest implements Serializable {
	private static final long serialVersionUID = 8712475929441655511L;

	private String _sPassword;
	private String _sColName;

	public ChangePasswordRequest( ) {
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.ChangePasswordHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.ChangePasswordResponse" );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.GenericRequest#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		Request r = super.copy( inputRequest );
		if( !( r instanceof ChangePasswordRequest ) ) {
			throw new IllegalArgumentException( inputRequest.getClass( ).getName( ) );
		}
		ChangePasswordRequest request = ( ChangePasswordRequest )r;
		request.setColName( _sColName );
		request.setPassword( _sPassword );
		return( request );
	}

	/**
	 * Returns password
	 * 
	 * @return the password
	 */
	public String getPassword( ) {
		return( _sPassword );
	}

	/**
	 * Returns password column name
	 * 
	 * @return the password column name
	 */
	public String getColName( ) {
		return( _sColName );
	}
	
	/**
	 * Sets password
	 * 
	 * @param sPassword
	 *            the password to set
	 */
	public void setPassword( String sPassword ) {
		_sPassword = sPassword;
	}

	/**
	 * Sets password column name
	 * 
	 * @param sColName
	 *            the password column name to set
	 */
	public void setColName( String sColName ) {
		_sColName = sColName;
	}
}
