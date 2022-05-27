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

import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.dataservice.shared.InsertRequest;
import org.homedns.mkh.dataservice.shared.ReturnValue;

/**
 * Insert request handler
 *
 */
public class InsertHandler extends GenericRequestHandler {

	public InsertHandler( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		InsertRequest insertRequest = ( InsertRequest )request;
		DataBuffer db = getDataBuffer( insertRequest );
		db.save( 
			DataBuffer.INSERT, 
			DataBuffer.JSON, 
			insertRequest.isBatchUpdate( ), 
			insertRequest.getData( ) 
		);
		Response response = createResponse( request );
		ReturnValue rv = new ReturnValue( );
		rv.addAll( db.getReturnValue( ) );
		response.setReturnValue( rv );		
		response.setRowCount( db.getRowCount( ) );
		response.setJsonData( db.getJson( ) );
		response.setResult( Response.SAVE_SUCCESS );
		return( response );
	}
}
