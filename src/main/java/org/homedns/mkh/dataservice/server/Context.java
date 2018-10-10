/*
 * Copyright 2013-2018 Mikhail Khodonov
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

import java.util.Locale;
import java.util.ResourceBundle;
import org.homedns.mkh.databuffer.Util;

/**
 * Application context object
 *
 */
public class Context {
	private static DataServiceImpl ds;

	/**
	 * Returns the data service servlet instance
	 * 
	 * @return the remote service servlet instance
	 */
	public static DataServiceImpl getInstance( ) {
		return( ds );
	}

	/**
	 * Sets data service servlet instance
	 * 
	 * @param ds
	 *            the data service servlet instance to set
	 */
	public static void setInstance( DataServiceImpl ds ) {
		Context.ds = ds;
	}
	
	/**
	 * Returns resource bundle for given locale.
	 * 
	 * @param locale
	 *            the locale object
	 * 
	 * @return resource bundle
	 */
	public static ResourceBundle getBundle( Locale locale ) {
		return( Util.getBundle( "org.homedns.mkh.dataservice.server.Messages", locale ) );
	}
	
	/**
	 * Returns localized message by specified key
	 * 
	 * @param sKey the key
	 * 
	 * @return the localized message
	 */
	public static String getLocalizedMsg( String sKey ) {
		Locale locale = null;
		if( ds.getDataBufferManager( ) == null ) {
			locale = Locale.getDefault( );
		} else {
			locale = ds.getDataBufferManager( ).getLocale( );
		}
		return( getBundle( locale ).getString( sKey ) );
	}
}
