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

package org.homedns.mkh.dataservice.server;

import java.security.Principal;

/**
 * User principal
 *
 */
public class UserPrincipal implements Principal {
	private String _sUser; 
	
	/**
	 * @param sUser
	 *            the user name
	 */
	public UserPrincipal( String sUser ) {
		_sUser = sUser;
	}
	
	/**
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName( ) {
		return( _sUser );
	}

	/**
	 * @see java.security.Principal#hashCode()
	 */
	@Override
	public int hashCode( ) {
		return( getName( ).hashCode( ) );
	}
	
	/**
	 * @see java.security.Principal#equals( Object obj )
	 */
	@Override
	public boolean equals( Object obj ) {
		boolean bEqual = false;
		if( obj instanceof UserPrincipal ) {
			UserPrincipal other = ( UserPrincipal )obj;
			bEqual = getName( ).equals( other.getName( ) );
		}
		return( bEqual );
	}
}
