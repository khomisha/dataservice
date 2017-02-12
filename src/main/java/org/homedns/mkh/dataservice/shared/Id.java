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

	private String sName;
	private Long lUID;

	public Id( ) { 
		lUID = Util.getUID( );
	}

	/**
	 * @param sName the name
	 */
	public Id( String sName ) { 
		this( );
		setName( sName );
	}

	/**
	 * Returns name
	 * 
	 * @return the name
	 */
	public String getName( ) {
		return( sName );
	}

	/**
	 * Sets name
	 * 
	 * @param sName
	 *            the name to set
	 */
	public void setName( String sName ) {
		this.sName = sName;
	}

	/**
	 * @return the unique number id
	 */
	public Long getUID( ) {
		return lUID;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString( ) {
		String s = ( sName == null ) ? "null" : sName;
		return( s + "-" + String.valueOf( lUID ) );
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode( ) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( lUID == null ) ? 0 : lUID.hashCode( ) );
		result = prime * result + ( ( sName == null ) ? 0 : sName.hashCode( ) );
		return( result );
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) {
			return( true );
		}
		if( obj == null ) {
			return( false );
		}
		if( getClass( ) != obj.getClass( ) ) {
			return( false );
		}
		Id other = ( Id )obj;
		if( lUID == null ) {
			if( other.lUID != null ) {
				return( false );
			}
		} else if( !lUID.equals( other.lUID ) ) {
			return( false );
		}
		if( sName == null ) {
			if( other.sName != null ) {
				return( false );
			}
		} else if( !sName.equals( other.sName ) ) {
			return( false );
		}
		return( true );
	}
}
