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
import org.homedns.mkh.dataservice.server.Context;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;

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
	 * @throws Exception
	 */
	protected DataBuffer getDataBuffer( Request request ) throws Exception {
		DataBuffer db = Context.getInstance( ).getDataBufferManager( ).getDataBuffer( 
			request.getID( ) 
		);
		return( db );
	}
	
	/**
	 * Closes specified data buffer and removes it from data buffer manager registry
	 * 
	 * @param request
	 *            the request object
	 */
	protected void closeDataBuffer( Request request ) {
		Context.getInstance( ).getDataBufferManager( ).closeDataBuffer( 
			request.getID( ) 
		);
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
		Response response = ( Response )clazz.newInstance( );
		if( request.getID( ) != null ) {
			response.setID( request.getID( ) );
		}
		return( response );
	}
}
