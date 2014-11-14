/*
 * Copyright 2014 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.client;

/**
 * Data types
 *
 */
public enum Type {
	STRING( "STRING" ), BYTE( "BYTE" ), SHORT( "SHORT" ), INT( "INT" ), LONG( "LONG" ),
	TIMESTAMP( "TIMESTAMP" ), DOUBLE( "DOUBLE" ), FLOAT( "FLOAT" ), BOOLEAN( "BOOLEAN" );

	private String _sName;
	
	/**
	 * @param sName the type name
	 */
	private Type( String sName ) {
		_sName = sName;
	}

	/**
	 * Returns type name
	 * 
	 * @return the type name
	 */
	public String getName( ) {
		return( _sName );
	}
}
