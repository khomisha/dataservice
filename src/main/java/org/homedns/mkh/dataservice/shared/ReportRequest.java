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
 * Report request
 *
 */
public class ReportRequest extends GenericRequest implements Serializable {
	private static final long serialVersionUID = 9119393937083854807L;

	private Data args;
	private String sDataBufferName;

	public ReportRequest( ) {
		setHandlerClassName( "org.homedns.mkh.dataservice.server.handler.ReportHandler" );
		setResponseClassName( "org.homedns.mkh.dataservice.shared.ReportResponse" );
		setInitPresenter( false );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.GenericRequest#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		Request r = super.copy( inputRequest );
		if( !( r instanceof ReportRequest ) ) {
			throw new IllegalArgumentException( inputRequest.getClass( ).getName( ) );
		}
		ReportRequest request = ( ReportRequest )r;
		request.setArgs( args );
		request.setDataBufferName( sDataBufferName );
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
	 * Returns data buffer name
	 * 
	 * @return the data buffer name
	 */
	public String getDataBufferName( ) {
		return( sDataBufferName );
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
	 * Sets data buffer name condition
	 * 
	 * @param sDataBufferName
	 *            the data buffer name to set
	 */
	public void setDataBufferName( String sDataBufferName ) {
		this.sDataBufferName = sDataBufferName;
	}
}
