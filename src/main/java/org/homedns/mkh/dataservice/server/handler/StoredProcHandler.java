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

package org.homedns.mkh.dataservice.server.handler;

import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.ReturnValue;
import org.homedns.mkh.dataservice.shared.StoredProcRequest;
import org.homedns.mkh.dataservice.shared.StoredProcResponse;

/**
 * Stored procedure request handler
 *
 */
public class StoredProcHandler extends GenericRequestHandler {

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		StoredProcResponse response = ( StoredProcResponse )createResponse( request );
//		response.setID( request.getID( ) );
		StoredProcRequest req = ( StoredProcRequest )request;
		DataBuffer db = getDataBuffer( req );
		db.insertData( req.getData( ) );
		if( req.isBatchUpdate( ) ) {
			db.saveBatch( DataBuffer.INSERT );			
		} else {
			db.save( DataBuffer.INSERT );
		}
		ReturnValue rv = new ReturnValue( );
		rv.addAll( db.getReturnValue( ) );
		response.setReturnValue( rv );		
		closeDataBuffer( request );
		response.setResult( Response.SAVE_SUCCESS );
		return( response );
	}
}
