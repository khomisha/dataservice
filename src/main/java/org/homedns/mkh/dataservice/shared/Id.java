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

package org.homedns.mkh.dataservice.shared;

import java.io.Serializable;

/**
 * Identification object
 *
 */
public class Id implements Serializable {
	private static final long serialVersionUID = 2891922995772739580L;

	private String _sName;
	private Long _lUID = Util.getUID( );

	public Id( ) { 
	}

	/**
	 * Returns name
	 * 
	 * @return the name
	 */
	public String getName( ) {
		return( _sName );
	}

	/**
	 * Sets name
	 * 
	 * @param sName
	 *            the name to set
	 */
	public void setName( String sName ) {
		_sName = sName;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode( ) {
		return( _sName.hashCode( ) + _lUID.hashCode( ) );
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object otherObj ) {
		if( this == otherObj ) {
			return( true );
		}
		if( otherObj == null ) {
			return( false );
		}
		if( getClass( ) != otherObj.getClass( ) ) {
			return( false );
		}
		Id other = ( Id )otherObj;
		return( _sName.equals( other._sName ) && _lUID.equals( other._lUID ) );
	}

	/**
	 * @return the unique number id
	 */
	public Long getUID( ) {
		return _lUID;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString( ) {
		String s = ( _sName == null ) ? "null" : _sName;
		return( s + "-" + String.valueOf( _lUID ) );
	}
}
