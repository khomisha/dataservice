/*
 * Copyright 2010-2014 Mikhail Khodonov
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
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.DBTransaction;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.DataBufferMetaData;
import org.homedns.mkh.databuffer.Environment;
import org.homedns.mkh.databuffer.InvalidDatabufferDesc;
import org.homedns.mkh.dataservice.shared.Id;
import org.homedns.mkh.dataservice.shared.Util;
import com.akiban.sql.StandardException;

/**
 * Data buffer manager implementation
 *
 */
public class DataBufferManager implements Environment {	
	private static final Logger LOG = Logger.getLogger( DataBufferManager.class );

	private ConcurrentHashMap< String, ConcurrentHashMap< Long, DataBuffer > > dbs = (
		new ConcurrentHashMap< String, ConcurrentHashMap< Long, DataBuffer > >( )
	);
	private DBTransaction sqlca; 
	private Locale locale; 
	private SimpleDateFormat cliDateFormat;
	private SimpleDateFormat srvDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	
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
		this.sqlca = sqlca;
		this.locale = locale;
		this.cliDateFormat = cliDateFormat;
	}
	
	/**
	* Closes data buffer manager and all bound data buffers
	*/
	public void close( ) {
		for( String sDataBufferName : dbs.keySet( ) ) {
			ConcurrentHashMap< Long, DataBuffer > dbMap = dbs.get( sDataBufferName );
			for( Long lUID : dbMap.keySet( ) ) {
				dbMap.get( lUID ).close( );
			}
			dbMap.clear( );
		}
		dbs.clear( );
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
	 * @throws InvalidDatabufferDesc
	 * @throws IOException
	 * @throws SQLException
	 * @throws StandardException
	 */
	public DataBuffer getDataBuffer( 
		Id id 
	) throws InvalidDatabufferDesc, IOException, SQLException, StandardException {
		String sDataBufferName = getLocaleDBName( id.getName( ) );
		DataBuffer db = getDataBuffer( sDataBufferName, id.getUID( ) );
		if( db == null ) {
			db = createDataBuffer( sDataBufferName, id.getUID( ), this );
		}
		return( db );
	}
	
	/**
	 * Returns report data buffer
	 * 
	 * @param sDataBufferName
	 *            the report data buffer name
	 * 
	 * @return the report data buffer
	 * 
	 * @throws InvalidDatabufferDesc 
	 * @throws StandardException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public DataBuffer getReportDataBuffer( 
		String sDataBufferName 
	) throws InvalidDatabufferDesc, IOException, SQLException, StandardException {
		DataBuffer db = null;
		sDataBufferName = getLocaleDBName( sDataBufferName );
		ConcurrentHashMap< Long, DataBuffer > dbMap = get( sDataBufferName );
		if( dbMap == null ) {
			db = createDataBuffer( sDataBufferName, Util.getUID( ), this );			
		} else {
			Long[] keys = dbMap.keySet( ).toArray( new Long[ dbMap.size( ) ] );
			db = getDataBuffer( sDataBufferName, keys[ 0 ] );
		}
		return( db );
	}
	
	/**
	 * Returns data buffer object if exists otherwise null.
	 * 
	 * @param sDataBufferName
	 *            the data buffer name
	 * @param lUID
	 *            the data buffer uid
	 * 
	 * @return data buffer object or null.
	 */
	private DataBuffer getDataBuffer( String sDataBufferName, Long lUID ) {
		DataBuffer db = null;
		ConcurrentHashMap< Long, DataBuffer > dbMap = get( sDataBufferName );
		if( dbMap != null ) {
			db = dbMap.get( lUID );
		}
		return( db );	
	}

	/**
	 * Creates data buffer.
	 * 
	 * @param sDataBufferName
	 *            the data buffer name
	 * @param lUID
	 *            the data buffer uid
	 * @param env
	 *            the application environment
	 * 
	 * @return data buffer object.
	 * 
	 * @throws InvalidDatabufferDesc
	 * @throws StandardException
	 * @throws SQLException
	 * @throws IOException
	 */
	protected DataBuffer createDataBuffer( 
		String sDataBufferName, 
		Long lUID,
		Environment env 
	) throws InvalidDatabufferDesc, IOException, SQLException, StandardException {
		DataBuffer db = new DataBuffer( new DataBufferMetaData( sDataBufferName, env ) );
		putDB( sDataBufferName, lUID, db );
		LOG.debug( "data buffer created: " + sDataBufferName + "-" + String.valueOf( lUID ) );
		return( db );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getDataBufferFilename(java.lang.String)
	 */
	@Override
	public String getDataBufferFilename( String sDataBufferName ) {
		return( Context.getInstance( ).getDataBufferFilename( sDataBufferName ) );
	}
	
	/**
	 * @see org.homedns.mkh.databuffer.Environment#getClientDateFormat()
	 */
	@Override
	public SimpleDateFormat getClientDateFormat( ) {
		return( cliDateFormat );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getServerDateFormat()
	 */
	@Override
	public SimpleDateFormat getServerDateFormat( ) {
		return( srvDateFormat );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getLocale()
	 */
	@Override
	public Locale getLocale( ) {
		return( locale );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getTransObject()
	 */
	@Override
	public DBTransaction getTransObject( ) {
		return( sqlca );
	}

	/**
	 * Closes specified data buffer and removes it from data buffer manager registry
	 * 
	 * @param id
	 *            the data buffer identifier
	 */
	public void closeDataBuffer( Id id ) {
		String sDataBufferName = getLocaleDBName( id.getName( ) );
		ConcurrentHashMap< Long, DataBuffer > dbMap = get( sDataBufferName );
		if( dbMap != null ) {
			DataBuffer db = dbMap.get( id.getUID( ) );
			if( db != null ) {
				dbMap.remove( id.getUID( ) );
				db.close( );
				LOG.debug( 
					"data buffer removed: " + sDataBufferName + "-" + String.valueOf( id.getUID( ) ) 
				);
			}
		}		
	}
	
	/**
	 * Returns data buffers map of the same kind
	 * 
	 * @param sDataBufferName
	 *            the data buffer name
	 * 
	 * @return the data buffers map of the same kind
	 */
	private ConcurrentHashMap< Long, DataBuffer > get( String sDataBufferName ) {
		return( dbs.get( sDataBufferName ) );
	}

	/**
	 * Returns data buffer name for current locale.
	 * 
	 * @param sDataBufferName
	 *            the data buffer name
	 * 
	 * @return data buffer name for current locale.
	 */
	private String getLocaleDBName( String sDataBufferName ) {
		if( sDataBufferName == null || "".equals( sDataBufferName ) ) {
			throw new IllegalArgumentException( sDataBufferName );
		}
		String sLocale = locale.getLanguage( );
		return(
			Util.DEFAULT_LOCALE.equals( sLocale ) || "".equals( sLocale ) ? 
			sDataBufferName : 
			sDataBufferName + "_" + sLocale
		);
	}

	/**
	 * Adds the databuffer object to the map.
	 * 
	 * @param sDataBufferName
	 *            the data buffer name
	 * @param lUID
	 *            the data buffer uid
	 * @param db
	 *            the data buffer object to add
	 */
	private void putDB( String sDataBufferName, Long lUID, DataBuffer db ) {
		ConcurrentHashMap< Long, DataBuffer > dbMap = get( sDataBufferName );
		if( dbMap == null ) {
			dbMap = new ConcurrentHashMap< Long, DataBuffer >( );
			ConcurrentHashMap< Long, DataBuffer > map = dbs.putIfAbsent( sDataBufferName, dbMap );
			if( map != null ) {
				dbMap = map;
			}
		}
		dbMap.putIfAbsent( lUID, db );
	}
}