/*
 * Copyright 2016 Mikhail Khodonov
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
 * Request to call stored procedure
 *
 */
public class StoredProcRequest extends GenericRequest {
	private static final long serialVersionUID = 4135129516012296125L;
	
	private Data data;

	public StoredProcRequest( ) {
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.StoredProcHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.StoredProcResponse" );
		setInitPresenter( false );
		data = new Data( );
	}

	/**
	 * Returns data object
	 * 
	 * @return the data object
	 */
	public Data getData( ) {
		return( data );
	}

	/**
	 * Adds parameter value
	 * 
	 * @param value the value to add
	 */
	public < T extends Serializable > void addValue( T value ) {
		data.addValue( value );
	}
	
	/**
	 * Returns specified parameter value
	 * 
	 * @param iIndex
	 *            the parameter index
	 * @return the parameter value
	 */
	public Object getValue( int iIndex ) {
		return( data.getValue( 0, iIndex ) );
	}
	
	/**
	 * Returns parameters count
	 * 
	 * @return the parameters count
	 */
	public int getParamCount( ) {
		return( data.get( 0 ).size( ) );
	}
	
	/**
	 * Clears data object
	 */
	public void clearData( ) {
		data.clear( );
	}
}
