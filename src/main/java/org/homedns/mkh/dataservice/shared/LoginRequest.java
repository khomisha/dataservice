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
 * Login request
 *
 */
public class LoginRequest extends GenericRequest implements Serializable {
	private static final long serialVersionUID = -7797256165791493976L;

	private String _sLogin;
	private String _sPassword;
	private String _sLocale;
	private String _sDateTimeFormat;
	
	public LoginRequest( ) {
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.LoginHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.LoginResponse" );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.GenericRequest#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		Request r = super.copy( inputRequest );
		if( !( r instanceof LoginRequest ) ) {
			throw new IllegalArgumentException( inputRequest.getClass( ).getName( ) );
		}
		LoginRequest request = ( LoginRequest )r;
		request.setDateTimeFormat( _sDateTimeFormat );
		request.setLocale( _sLocale );
		request.setLogin( _sLogin );
		request.setPassword( _sPassword );
		return( request );
	}

	/**
	 * Returns login
	 * 
	 * @return the login
	 */
	public String getLogin( ) {
		return( _sLogin );
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
	 * Returns locale
	 * 
	 * @return the locale
	 */
	public String getLocale( ) {
		return( _sLocale );
	}

	/**
	 * Returns datetime format
	 * 
	 * @return the datetime format
	 */
	public String getDateTimeFormat( ) {
		return( _sDateTimeFormat );
	}

	/**
	 * Sets login
	 * 
	 * @param sLogin
	 *            the login to set
	 */
	public void setLogin( String sLogin ) {
		_sLogin = sLogin;
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
	 * Sets locale
	 * 
	 * @param sLocale
	 *            the locale to set
	 */
	public void setLocale( String sLocale ) {
		_sLocale = sLocale;
	}

	/**
	 * Sets datetime format
	 * 
	 * @param sDateTimeFormat
	 *            the datetime format to set
	 */
	public void setDateTimeFormat( String sDateTimeFormat ) {
		_sDateTimeFormat = sDateTimeFormat;
	}
}
