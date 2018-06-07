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

package org.homedns.mkh.dataservice.server.handler;

import java.io.Serializable;
import java.util.List;

import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.RetrieveRequest;
import org.homedns.mkh.dataservice.shared.RetrieveResponse;

/**
 * Retrieve request handler
 *
 */
public class RetrieveHandler extends GenericRequestHandler {

	public RetrieveHandler( ) { }

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		RetrieveRequest retrieveRequest = ( RetrieveRequest )request;
		DataBuffer db = getDataBuffer( retrieveRequest );
		List< Serializable > args = ( 
			retrieveRequest.getArgs( ) == null ? 
			null : 
			retrieveRequest.getArgs( ).get( 0 )
		);
		String sCondition = retrieveRequest.getCondition( );
		int iRowCount;
		if( sCondition == null ) {
			iRowCount = ( args == null ) ? db.retrieve( ) : db.retrieve( args );
		} else {
			iRowCount = ( args == null ) ? db.retrieve( sCondition ) : db.retrieve( args, sCondition );
		}
		RetrieveResponse response = ( RetrieveResponse )createResponse( request );
		response.setRowCount( iRowCount );
		response.setJsonData( db.getJson( ) );
		response.setForcedRetrieve( retrieveRequest.isForcedRetrieve( ) );
		if( !retrieveRequest.isInitPresenter( ) ) {
			response.setDataBufferDesc( db.getDescriptionAsJson( ) );
			response.setServerPaging( db.getPageSize( ) > 0 );						
		}
		response.setResult( Response.SUCCESS );
		return( response );
	}
}
