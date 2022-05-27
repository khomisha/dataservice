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
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.dataservice.shared.ChangePasswordRequest;

/**
 * Change password request handler
 *
 */
public class ChangePasswordHandler extends GenericRequestHandler {

	public ChangePasswordHandler( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		ChangePasswordRequest changeRequest = ( ChangePasswordRequest )request;
		DataBuffer db = getDataBuffer( changeRequest );
		Serializable[] row = db.getRowData( 0 );
		int iColNum = db.getDescription( ).getColumn( changeRequest.getColName( ) ).getColNum( );
		row[ iColNum ] = changeRequest.getPassword( );
		db.save( 
			DataBuffer.UPDATE, 
			DataBuffer.SERIALIZABLE_ARRAY, 
			changeRequest.isBatchUpdate( ), 
			new Serializable[][] { row }
		);
		Response response = createResponse( request );
		response.setResult( Response.SUCCESS );
		return( response );
	}
}
