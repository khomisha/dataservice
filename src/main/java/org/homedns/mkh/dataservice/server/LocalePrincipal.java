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
 * $Id: LocalePrincipal.java 3 2013-07-16 15:46:34Z khomisha $
 */

package org.homedns.mkh.dataservice.server;

import java.security.Principal;
import java.util.Locale;

/**
 * Locale principal
 *
 */
public class LocalePrincipal implements Principal {
	private Locale _locale;
	
	/**
	 * @param locale
	 *            the subject locale
	 */
	public LocalePrincipal( Locale locale ) {
		_locale = locale;
	}
	
	/**
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName( ) {
		return( _locale.getLanguage( ) );
	}

	/**
	 * @see java.security.Principal#hashCode()
	 */
	@Override
	public int hashCode( ) {
		return( _locale.hashCode( ) + getName( ).hashCode( ) );
	}
	
	/**
	 * @see java.security.Principal#equals( Object obj )
	 */
	@Override
	public boolean equals( Object obj ) {
		boolean bEqual = false;
		if( obj instanceof LocalePrincipal ) {
			LocalePrincipal other = ( LocalePrincipal )obj;
			bEqual = getName( ).equals( other.getName( ) );
		}
		return( bEqual );
	}
	
	/**
	 * Returns locale
	 * 
	 * @return the locale
	 */
	public Locale getLocale( ) {
		return( _locale );
	}
}
