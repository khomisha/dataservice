/*
 * Copyright 2013-2014 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.server;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Download file service
 */
@SuppressWarnings( "serial" )
public abstract class DownloadService extends HttpServlet {
	private BufferedInputStream _input;
	private int _iBuffSize = 32768;

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet( 
		HttpServletRequest request, 
		HttpServletResponse response 
	) throws ServletException, IOException {
		try {
			setResponseParams( request, response );
			write2Response( _input, response );
		} catch( Exception e ) {
			throw new ServletException( e );
		}
	}

	/**
	 * Sets response parameters.
	 * 
	 * @param request
	 *            the client request object
	 * @param response
	 *            the response object
	 * 
	 * @throws IOException
	 */
	protected abstract void setResponseParams( 
		HttpServletRequest request, 
		HttpServletResponse response 
	) throws IOException;

	/**
	 * Sets buffered input stream.
	 * 
	 * @param input
	 *            the buffered input stream
	 */
	protected void setInputStream( BufferedInputStream input ) {
		_input = input;
	}

	/**
	 * Sets input stream buffer size.
	 * 
	 * @param iBuffSize
	 *            the buffer size
	 */
	protected void setBuffSize( int iBuffSize ) {
		_iBuffSize = iBuffSize;
	}

	/**
	 * Returns input stream buffer size.
	 * 
	 * @return the buffer size
	 */
	protected int getBuffSize( ) {
		return( _iBuffSize );
	}

	/**
	 * Writes to response stream file's content.
	 * 
	 * @param input
	 *            the buffered input stream
	 * @param response
	 *            http servlet response
	 * 
	 * @throws IOException
	 */
	private void write2Response( 
		BufferedInputStream input, 
		HttpServletResponse response 
	) throws IOException {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream( );
			byte[] ab = new byte[ _iBuffSize ];
			int iCount;
			while( ( iCount = input.read( ab, 0, _iBuffSize ) ) >= 0 ) {
				out.write( ab, 0, iCount );
			}
		}
		finally {
			if( input != null ) {
				input.close( );
			}
			if( out != null ) {
				out.flush( );
				out.close( );
			}
		}
	}
}
