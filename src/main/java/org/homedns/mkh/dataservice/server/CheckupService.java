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
import java.util.Locale;
import java.util.Map;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Checkup service validates user and checks login attempt count
 *
 */
public interface CheckupService {
	
	/**
	 * Returns true if given client's IP is locked and false otherwise. Locked
	 * client means that attempt number more than defined attempt threshold.
	 * Threshold must be > 0.
	 * 
	 * @return true if given client IP is locked and false otherwise.
	 */
	public boolean isLocked( );
	
	/**
	* Increments attempts count value
	*/
	public void incrementCount( );

	/**
	* Resets attempts count.
	*/
	public void resetCount( );

	/**
	 * Authenticates user.
	 * 
	 * @param sLogin
	 *            the user login
	 * @param sPassword
	 *            the user password
	 * 
	 * @return true if authentication is success false otherwise
	 * 
	 * @throws Exception
	 */
	public boolean isValidUser( String sLogin, String sPassword ) throws Exception;

	/**
	 * Inits service.
	 * 
	 * @param options
	 *            the login options
	 * @param dataSource
	 *            the data source
	 * @param locale
	 *            the current locale
	 * @param dateFormat
	 *            the date format
	 * 
	 * @throws NamingException
	 * @throws SQLException
	 */
	public void init( 
		Map< String, ? > options, 
		DataSource dataSource,
		Locale locale, 
		SimpleDateFormat dateFormat 
	) throws NamingException, SQLException;

	/**
	 * Returns login options.
	 * 
	 * @return the login options
	 */
	public Map< String, ? > getOptions( );
	
	/**
	 * Closes service and saves login attempts count.
	 * 
	 * @throws SQLException
	 */
	public void close( );
}
