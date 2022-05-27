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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.databuffer.api.DataBufferManager;
import java.io.Serializable;

/**
 * Abstract checkup service implementation
 *
 */
public abstract class AbstractCheckupService implements CheckupService {
	private static final Logger LOG = Logger.getLogger( AbstractCheckupService.class );

	private Map< String, ? > options;
	private int iThreshold;
	private int iLoginCount;
	private DataBufferManager dbm;

	public AbstractCheckupService( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#isLocked()
	 */
	@Override
	public boolean isLocked( ) {		
		return( iThreshold > 0 && iLoginCount > iThreshold );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#incrementCount()
	 */
	@Override
	public void incrementCount( ) {
		if( iThreshold > 0 ) {
			iLoginCount++;
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#resetCount()
	 */
	@Override
	public void resetCount( ) {
		if( iThreshold > 0 ) {
			iLoginCount = 0;
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#isValidUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isValidUser( String sLogin, String sPassword ) throws Exception {
		int iRowCount = 0;
		// see also acr_user_login.dbuf for example
		try( 
			DataBuffer db = dbm.getDataBuffer( 
				( String )options.get( "login_db" ), 
				( String )options.get( "jdbc_login_resource_name" ) 
			) 
		) {
			iRowCount = db.retrieve( Arrays.asList( new Serializable[] { sPassword, sLogin } ) );
			LOG.debug( "user: " + sLogin + " login success: " + String.valueOf( iRowCount > 0 ) );
		}
		return( iRowCount > 0 );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#init(java.util.Map, javax.sql.DataSource, java.util.Locale, java.text.SimpleDateFormat)
	 */
	@Override
	public void init( Map< String, ? > options, DataBufferManager dbm ) throws NamingException, SQLException {
		this.options = options;
		this.dbm = dbm;
		iThreshold = Integer.valueOf( ( String )options.get( "attempt_threshold" ) );
		iLoginCount = ( iThreshold > 0 ) ? retrieveLoginCount( ) : 0;
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#getDataBufferManager()
	 */
	@Override
	public DataBufferManager getDataBufferManager( ) {
		return( dbm );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#getOptions()
	 */
	@Override
	public Map< String, ? > getOptions( ) {
		return( options );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#close()
	 */
	@Override
	public abstract void close( );

	/**
	 * Returns login count
	 * 
	 * @return the login count
	 */
	public int getLoginCount( ) {
		return( iLoginCount );
	}

	/**
	* Returns retrieved login attempts count.
	*
	* @return the login attempts count
	* 
	* @throws SQLException 
	*/
	protected abstract int retrieveLoginCount( ) throws SQLException;
}
