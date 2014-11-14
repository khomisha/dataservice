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

import java.sql.SQLException;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.LoadPageRequest;

/**
 * Load page request handler
 *
 */
public class LoadPageHandler extends GenericRequestHandler {

	public LoadPageHandler( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		LoadPageRequest pageRequest = ( LoadPageRequest )request;
		DataBuffer db = getDataBuffer( pageRequest );
		boolean bFound = false;
		int iPageNum = pageRequest.getLoadingPageNum( );
		int iCurrentPage = db.getPage( );
		Response response = createResponse( request );
		if( iPageNum == iCurrentPage ) {
			if( db.nextPage( ) ) {
				db.previousPage( );
			} else if( db.previousPage( ) ) {
				db.nextPage( );
			}
			putPageData( response, db );
		} else if( iPageNum > iCurrentPage ) {
			while( db.nextPage( ) ) {
				bFound = ( iPageNum == db.getPage( ) );
				if( bFound ) {
					putPageData( response, db );
					break;
				}
			}
			if( !bFound ) {
				// out of boundaries, put max page
				putPageData( response, db );
			}
		} else if( iPageNum < iCurrentPage ) {
			while( db.previousPage( ) ) {
				bFound = ( iPageNum == db.getPage( ) );
				if( bFound ) {
					putPageData( response, db );
					break;
				}
			}			
			if( !bFound ) {
				// out of boundaries, put min page
				putPageData( response, db );
			}
		}
		if( !pageRequest.isInitPresenter( ) ) {
			response.setDataBufferDesc( db.getDescriptionAsJson( ) );
			response.setServerPaging( db.getPageSize( ) > 0 );			
		}
		response.setResult( Response.SUCCESS );
		return( response );
	}

	/**
	 * Puts found page data to the response.
	 * 
	 * @param response
	 *            the response
	 * @param db
	 *            the data buffer
	 * 
	 * @throws SQLException
	 */
	private void putPageData( Response response, DataBuffer db ) throws SQLException {
		response.setJsonData( db.getJson( ) );
		response.setPageNum( db.getPage( ) );
	}
}
