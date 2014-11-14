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

package org.homedns.mkh.dataservice.shared;

import java.io.Serializable;

/**
 * Retrieve request
 *
 */
public class RetrieveRequest extends GenericRequest implements Serializable {
	private static final long serialVersionUID = -546028951543866857L;

	private Data _args;
	private String _sCondition;
	
	public RetrieveRequest( ) { 
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.RetrieveHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.GenericResponse" );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.GenericRequest#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		Request r = super.copy( inputRequest );
		if( !( r instanceof RetrieveRequest ) ) {
			throw new IllegalArgumentException( inputRequest.getClass( ).getName( ) );
		}
		RetrieveRequest request = ( RetrieveRequest )r;
		request.setArgs( _args );
		request.setCondition( _sCondition );
		return( request );
	}

	/**
	 * Returns retrieval arguments
	 * 
	 * @return the retrieval arguments
	 */
	public Data getArgs( ) {
		return( _args );
	}

	/**
	 * Returns filter condition
	 * 
	 * @return the filter condition
	 */
	public String getCondition( ) {
		return( _sCondition );
	}

	/**
	 * Sets retrieval arguments
	 * 
	 * @param args
	 *            the retrieval arguments to set
	 */
	public void setArgs( Data args ) {
		_args = args;
	}

	/**
	 * Sets filter condition
	 * 
	 * @param sCondition
	 *            the filter condition to set
	 */
	public void setCondition( String sCondition ) {
		_sCondition = sCondition;
	}
}
