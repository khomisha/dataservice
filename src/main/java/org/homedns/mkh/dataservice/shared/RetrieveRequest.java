/*
 * Copyright 2014-2018 Mikhail Khodonov
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

	private Data args;
	private String sCondition;
	private boolean bForcedRetrieve = false;
	
	public RetrieveRequest( ) { 
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.RetrieveHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.RetrieveResponse" );
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
		request.setArgs( args );
		request.setCondition( sCondition );
		return( request );
	}

	/**
	 * Returns retrieval arguments
	 * 
	 * @return the retrieval arguments
	 */
	public Data getArgs( ) {
		return( args );
	}

	/**
	 * Returns filter condition
	 * 
	 * @return the filter condition
	 */
	public String getCondition( ) {
		return( sCondition );
	}

	/**
	 * Sets retrieval arguments
	 * 
	 * @param args
	 *            the retrieval arguments to set
	 */
	public void setArgs( Data args ) {
		this.args = args;
	}

	/**
	 * Sets filter condition
	 * 
	 * @param sCondition
	 *            the filter condition to set
	 */
	public void setCondition( String sCondition ) {
		this.sCondition = sCondition;
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.view.View#isForcedRetrieve()
	 */
	public boolean isForcedRetrieve( ) {
		return( bForcedRetrieve );
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.view.View#setForcedRetrieve(boolean)
	 */
	public void setForcedRetrieve( boolean bForcedRetrieve ) {
		this.bForcedRetrieve = bForcedRetrieve;
	}
}
