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

import java.security.Principal;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.dataservice.shared.Id;

/**
 * Access rights principal (access rights data buffer identity)
 *
 */
public class AccessRightsPrincipal implements Principal {
	private DataBuffer _db;
	private Id _id;
		
	/**
	 * @param db
	 *            the access rights data buffer
	 * @param id
	 *            the data buffer unique identifier object
	 */
	public AccessRightsPrincipal( DataBuffer db, Id id ) {
		_db = db;
		_id = id;
	}

	/**
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName( ) {
		return( _db.getDataBufferName( ) );
	}

	public Id getID( ) {
		return( _id );
	}
	
	/**
	 * Returns access rights data buffer
	 * 
	 * @return the access rights data buffer
	 */
	public DataBuffer getDataBuffer( ) {
		return( _db );
	}

	/**
	 * @see java.security.Principal#hashCode()
	 */
	@Override
	public int hashCode( ) {
		return( getID( ).hashCode( ) );
	}
	
	/**
	 * @see java.security.Principal#equals( Object obj )
	 */
	@Override
	public boolean equals( Object obj ) {
		boolean bEqual = false;
		if( obj instanceof AccessRightsPrincipal ) {
			AccessRightsPrincipal other = ( AccessRightsPrincipal )obj;
			bEqual = getID( ).equals( other.getID( ) );
		}
		return( bEqual );
	}
}
