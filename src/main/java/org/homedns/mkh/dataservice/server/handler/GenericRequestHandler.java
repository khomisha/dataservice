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

import java.io.IOException;
import java.sql.SQLException;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.InvalidDatabufferDesc;
import org.homedns.mkh.dataservice.server.Context;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import com.akiban.sql.StandardException;

/**
 * Generic request handler
 *
 */
public abstract class GenericRequestHandler implements RequestHandler {

	public GenericRequestHandler( ) { 
	}

	/**
	 * Returns data buffer
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the data buffer
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws StandardException
	 * @throws InvalidDatabufferDesc
	 */
	protected DataBuffer getDataBuffer( 
		Request request 
	) throws IOException, SQLException, StandardException, InvalidDatabufferDesc {
		DataBuffer db = Context.getInstance( ).getDataBufferManager( ).getDataBuffer( 
			request.getID( ) 
		);
		return( db );
	}
	
	/**
	 * Creates response object
	 * 
	 * @param request
	 *            the request
	 * @return the response
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected Response createResponse( 
		Request request 
	) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class< ? > clazz = Class.forName( request.getResponseClassName( ) );
		return( ( Response )clazz.newInstance( ) );
	}
}
