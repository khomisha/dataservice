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
import java.util.ArrayList;
import java.util.List;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.databuffer.api.DataBufferManager;
import org.homedns.mkh.dataservice.server.Context;
import org.homedns.mkh.dataservice.server.report.Excel;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.ReportRequest;

/**
 * Report request handler
 *
 */
public class ReportHandler extends GenericRequestHandler {
	private DataBufferManager dbm;

	public ReportHandler( ) { 
	}

	/**
	 * @see org.homedns.mkh.dataservice.server.handler.RequestHandler#execute(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Response execute( Request request ) throws Exception {
		dbm = Context.getInstance( ).getDataBufferManager( );
		ReportRequest reportRequest = ( ReportRequest )request;
		List< Serializable > args = new ArrayList< Serializable >( );
		args.add( reportRequest.getDataBufferName( ) );
		DataBuffer db = dbm.getDataBuffer( reportRequest.getDataBufferName( ) );
		int iResult;
		if( reportRequest.getArgs( ) == null || reportRequest.getArgs( ).isEmpty( ) ) {	
			iResult = db.retrieve( );
		} else {
			args = reportRequest.getArgs( ).get( 0 );
			iResult = db.retrieve( args );
		}
		Response response = createResponse( request );
//		response.setID( request.getID( ) );
		String sFile = (
			( DataBufferManager.DEFAULT_LOCALE.equals( dbm.getLocale( ) ) ) ? 
			db.getDataBufferName( ) + "_template.xls" : 
			db.getDataBufferName( ) + "_" + dbm.getLocale( ).getLanguage( ) + "_template.xls"
		);
		Excel excel = new Excel( sFile, db );
		response.setDownloadFileName( excel.getExcelFile( ) );
		response.setResult( iResult );
		return( response );
	}
}
