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

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.DateTimeFormatInfo;

/**
 * Date formatter object
 *
 */
public enum DateFormatter {
	DATE( 0 ), TIME( 1 ), TIMESTAMP( 2 );

	private DateTimeFormat _dateFormatter;

	/**
	 * @param iStyle date/time representation 0 - DATE, 1 - TIME, 2 - TIMESTAMP
	 */
	private DateFormatter( int iStyle ) {
		DateTimeFormatInfo fmt = LocaleInfo.getCurrentLocale( ).getDateTimeFormatInfo( );
		if( iStyle == 0 ) {
			_dateFormatter = DateTimeFormat.getFormat( fmt.dateFormatShort( ) );
		} else if( iStyle == 1 ) {
			_dateFormatter = DateTimeFormat.getFormat( fmt.timeFormat( ) );
		} else if( iStyle == 2 ) {
			_dateFormatter = DateTimeFormat.getFormat( fmt.dateFormatShort( ) + " " + fmt.timeFormat( ) );
		}
	}
	
	/**
	 * Returns date time format
	 * 
	 * @return the date time format
	 */
	public DateTimeFormat getDateTimeFormat( ) {
		return( _dateFormatter );
	}
}
