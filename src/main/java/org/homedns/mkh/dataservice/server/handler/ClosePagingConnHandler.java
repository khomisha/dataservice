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

import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.dataservice.shared.ClosePagingConnRequest;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;

/**
 * Close server paging connection handler
 *
 */
public class ClosePagingConnHandler extends GenericRequestHandler {

	public ClosePagingConnHandler( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		DataBuffer db = getDataBuffer( ( ClosePagingConnRequest )request );
		db.closeConn( );
		Response response = createResponse( request );
		response.setResult( Response.SUCCESS );
		return( response );
	}
}
