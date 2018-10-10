/*
 * Copyright 2010-2018 Mikhail Khodonov
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

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

import org.homedns.mkh.databuffer.AbstractDataBufferManager;
import org.homedns.mkh.databuffer.DBTransaction;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.UUID;
import org.homedns.mkh.dataservice.shared.Id;
import org.homedns.mkh.dataservice.shared.Util;

/**
 * Data buffer manager implementation
 *
 */
public class DataBufferManager extends AbstractDataBufferManager {	
	
	/**
	 * @param sqlca
	 *            the transaction object
	 * @param locale
	 *            the current client locale
	 * @param cliDateFormat
	 *            current client date time format
	 * 
	 * @throws IOException
	 */
	public DataBufferManager( 
		DBTransaction sqlca, Locale locale, SimpleDateFormat cliDateFormat 
	) throws IOException {
		setTransObject( sqlca );
		setLocale( locale );
		setClientDateFormat( cliDateFormat );
	}
	
	/**
	 * Returns report data buffer
	 * 
	 * @param sDataBufferName
	 *            the report data buffer name
	 * 
	 * @return the report data buffer
	 * 
	 * @throws Exception 
	 */
	public DataBuffer getReportDataBuffer( String sDataBufferName ) throws Exception {
		DataBuffer db = null;
		sDataBufferName = getLocaleDBName( sDataBufferName );
		ConcurrentHashMap< Long, DataBuffer > dbMap = get( sDataBufferName );
		if( dbMap == null ) {
			db = createDataBuffer( sDataBufferName, Util.getUID( ) );			
		} else {
			Long[] keys = dbMap.keySet( ).toArray( new Long[ dbMap.size( ) ] );
			db = getDataBuffer( sDataBufferName, keys[ 0 ] );
		}
		return( db );
	}

	/**
	 * @see org.homedns.mkh.databuffer.AbstractDataBufferManager#getDataBufferFilename(java.lang.String)
	 */
	@Override
	public String getDataBufferFilename( String sDataBufferName ) {
		return( Context.getInstance( ).getDataBufferFilename( sDataBufferName ) );
	}

	/**
	 * Returns data buffer (if it doesn't exist try to create it and adds to the
	 * databuffer map).
	 * 
	 * @param id
	 *            the identifier object
	 * 
	 * @return the data buffer object or null
	 * 
	 * @throws Exception
	 */
	public DataBuffer getDataBuffer( Id id ) throws Exception {
		return super.getDataBuffer( new UID( id.getName( ), id.getUID( ) ) );
	}

	/**
	 * Closes specified data buffer and removes it from data buffer manager registry
	 * 
	 * @param id
	 *            the data buffer identifier
	 */
	public void closeDataBuffer( Id id ) {
		super.closeDataBuffer( new UID( id.getName( ), id.getUID( ) ) );
	}
	
	@SuppressWarnings( "serial" )
	private class UID implements UUID {
		private String sName;
		private Long lUID;
		
		/**
		 * @param sName the name
		 * @param lUID the unique number
		 */
		public UID( String sName, Long lUID ) {
			this.sName = sName;
			this.lUID = lUID;
		}

		/**
		 * @see org.homedns.mkh.databuffer.UUID#getName()
		 */
		@Override
		public String getName( ) {
			return( sName );
		}

		/**
		 * @see org.homedns.mkh.databuffer.UUID#setName(java.lang.String)
		 */
		@Override
		public void setName( String sName ) {
			this.sName = sName;
		}

		/**
		 * @see org.homedns.mkh.databuffer.UUID#getUID()
		 */
		@Override
		public Long getUID( ) {
			return( lUID );
		}
	}
}