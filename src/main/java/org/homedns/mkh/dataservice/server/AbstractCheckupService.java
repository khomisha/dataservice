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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.DBTransaction;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.DataBufferMetaData;
import org.homedns.mkh.databuffer.Environment;
import org.homedns.mkh.databuffer.InvalidDatabufferDesc;
import com.akiban.sql.StandardException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract checkup service implementation
 *
 */
public abstract class AbstractCheckupService implements CheckupService, Environment {
	private static final Logger LOG = Logger.getLogger( AbstractCheckupService.class );

	private Map< String, ? > _options;
	private int _iThreshold;
	private int _iLoginCount = 0;
	private DBTransaction _sqlca;
	private SimpleDateFormat _dateClientFormat;
	private SimpleDateFormat _dateServerFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	private Locale _locale;

	public AbstractCheckupService( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#isLocked()
	 */
	@Override
	public boolean isLocked( ) {		
		return( _iThreshold > 0 && _iLoginCount > _iThreshold );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#incrementCount()
	 */
	@Override
	public void incrementCount( ) {
		if( _iThreshold > 0 ) {
			_iLoginCount++;
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#resetCount()
	 */
	@Override
	public void resetCount( ) {
		if( _iThreshold > 0 ) {
			_iLoginCount = 0;
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#isValidUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isValidUser( 
		String sLogin, 
		String sPassword 
	) throws InvalidDatabufferDesc, IOException, SQLException, StandardException {
		DataBuffer db = null;
		int iRowCount = 0;
		try {
			// see also acr_user_login.dbuf for example
			db = new DataBuffer( 
				new DataBufferMetaData( ( String )_options.get( "login_db" ), this ) 
			);
			iRowCount = db.retrieve( Arrays.asList( new Serializable[] { sPassword, sLogin } ) );
			LOG.debug( "user: " + sLogin + " login success: " + String.valueOf( iRowCount > 0 ) );
		}
		finally {
			if( db != null ) {
				db.close( );
			}
		}
		return( iRowCount > 0 );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#init(java.util.Map, javax.sql.DataSource, java.util.Locale, java.text.SimpleDateFormat)
	 */
	@Override
	public void init( 
		Map< String, ? > options, 
		DataSource dataSource, 
		Locale locale, 
		SimpleDateFormat dateClientFormat 
	) throws NamingException, SQLException {
		_options = options;
		_iThreshold = Integer.valueOf( ( String )options.get( "attempt_threshold" ) );
		_sqlca = new DBTransaction( dataSource );
		_locale = locale;
		_dateClientFormat = dateClientFormat;
		if( _iThreshold > 0 ) {
			_iLoginCount = retrieveLoginCount( );
		}
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#getOptions()
	 */
	@Override
	public Map< String, ? > getOptions( ) {
		return( _options );
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.CheckupService#close()
	 */
	@Override
	public abstract void close( );
	
	/**
	 * @see org.homedns.mkh.databuffer.Environment#getClientDateFormat()
	 */
	@Override
	public SimpleDateFormat getClientDateFormat( ) {
		return( _dateClientFormat );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getServerDateFormat()
	 */
	@Override
	public SimpleDateFormat getServerDateFormat( ) {
		return( _dateServerFormat );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getTransObject()
	 */
	@Override
	public DBTransaction getTransObject( ) {
		return( _sqlca );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getDataBufferFilename(java.lang.String)
	 */
	@Override
	public String getDataBufferFilename( String sDataBufferName ) {
		return( Context.getInstance( ).getDataBufferFilename( sDataBufferName ) );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getLocale()
	 */
	@Override
	public Locale getLocale( ) {
		return( _locale );
	}

	/**
	 * Returns login count
	 * 
	 * @return the login count
	 */
	public int getLoginCount( ) {
		return( _iLoginCount );
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
