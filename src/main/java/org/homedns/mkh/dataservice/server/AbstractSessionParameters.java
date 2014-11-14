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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.homedns.mkh.databuffer.SessionParameters;

/**
 * Abstract database session parameters
 *
 */
public abstract class AbstractSessionParameters implements SessionParameters {
	private Map< String, String > _names = new HashMap< String, String >( );
	private Map< String, String > _values = new HashMap< String, String >( );
	
	/**
	 * @see org.homedns.mkh.databuffer.SessionParameters#set2Session(java.sql.Connection)
	 */
	@Override
	public abstract void set2Session( Connection conn ) throws SQLException;

	/**
	 * @see org.homedns.mkh.databuffer.SessionParameters#setParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public void setParameter( String sKey, String sName ) {
		_names.put( sKey, sName );
	}

	/**
	 * @see org.homedns.mkh.databuffer.SessionParameters#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter( String sKey ) {
		return( _names.get( sKey ) );
	}

	/**
	 * @see org.homedns.mkh.databuffer.SessionParameters#setParameterValue(java.lang.String, java.lang.String)
	 */
	@Override
	public void setParameterValue( String sKey, String sValue ) {
		_values.put( sKey, sValue );
	}

	/**
	 * @see org.homedns.mkh.databuffer.SessionParameters#getParameterValue(java.lang.String)
	 */
	@Override
	public String getParameterValue( String sKey ) {
		return( _values.get( sKey ) );
	}
}
