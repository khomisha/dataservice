/*
 * Copyright 2010-2022 Mikhail Khodonov
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

import java.util.concurrent.ConcurrentHashMap;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.api.BaseDataBufferManager;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.dataservice.shared.Id;

/**
 * Data buffer manager
 *
 */
public class DBManager extends BaseDataBufferManager {	
	private static final Logger LOG = Logger.getLogger( DBManager.class );

	private ConcurrentHashMap< String, ConcurrentHashMap< Long, DataBuffer > > dbMap;
	
	/**
	 * @throws SQLException
	 */
	public DBManager( ) throws SQLException {
		super( );
		dbMap = new ConcurrentHashMap< >( );
	}

	/**
	 * Returns data buffer (if it doesn't exist try to create it and adds to the
	 * databuffer map).
	 * 
	 * @param id
	 *            the identifier object
	 * 
	 * @return the data buffer object
	 * 
	 * @throws Exception
	 */
	public DataBuffer getDataBuffer( Id id ) throws Exception {
		DataBuffer db = null;
		ConcurrentHashMap< Long, DataBuffer > databuffers = dbMap.get( id.getName( ) );
		if( databuffers != null ) {
			db = databuffers.get( id.getUID( ) );
			if( db != null ) {
				return( db );
			}
		} else {
			databuffers = new ConcurrentHashMap< >( );
			dbMap.put( id.getName( ), databuffers );
		}
		db = super.getDataBuffer( id.getName( ) );
		databuffers.put( id.getUID( ), db );
		return( db );
	}

	/**
	 * Closes specified data buffer and removes it from data buffer manager registry
	 * 
	 * @param id
	 *            the data buffer identifier
	 *            
	 * @throws Exception 
	 */
	public void closeDataBuffer( Id id ) throws Exception {
		ConcurrentHashMap< Long, DataBuffer > databuffers = dbMap.get( id.getName( ) );
		if( databuffers != null ) {
			DataBuffer db = databuffers.get( id.getUID( ) );
			if( db != null ) {
				databuffers.remove( id.getUID( ) );
				db.close( );
			}
		}		
	}
	
	/**
	 * Closes data buffer manager and all bound data buffers
	 */
	public void close( ) {
		try {
			for( String sDataBufferName : dbMap.keySet( ) ) {
				ConcurrentHashMap< Long, DataBuffer > databuffers = dbMap.get( sDataBufferName );
				for( Long lUID : databuffers.keySet( ) ) {
					databuffers.get( lUID ).close( );
				}
				databuffers.clear( );
			}
			dbMap.clear( );
		}
		catch( Exception e ) {
			dbMap = null;
			LOG.error( e.getMessage( ), e );
		}
	}
}