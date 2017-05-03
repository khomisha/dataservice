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

package org.homedns.mkh.dataservice.shared;

import java.io.Serializable;

/**
 * RPC call response interface
 *
 */
public interface Response extends Serializable {
	public static final Integer SUCCESS 		= 0;
	public static final Integer SAVE_SUCCESS 	= 1;
	public static final Integer FAILURE 		= -1;

	/**
	 * Returns response data
	 * 
	 * @return the data
	 */
	public String[][] getData( );
	
	/**
	 * Sets response data
	 * 
	 * @param asData
	 *            the data to set
	 */
	public void setData( String[][] asData );

	/**
	 * Returns data as json string
	 * 
	 * @return the data as json string
	 */
	public String getJsonData( );
	
	/**
	 * Sets data as json string
	 * 
	 * @param sData
	 *            the the data as json string to set
	 */
	public void setJsonData( String sData );
	
	/**
	 * Returns data buffer description as json string
	 * 
	 * @return the data buffer description
	 */
	public String getDataBufferDesc( );
	
	/**
	 * Sets data buffer description as json string
	 * 
	 * @param sDataBufferDesc
	 *            the data buffer description to set
	 */
	public void setDataBufferDesc( String sDataBufferDesc );

	/**
	 * Returns RPC call result
	 * 
	 * @return the RPC call result
	 */
	public Integer getResult( );

	/**
	 * Sets RPC call result
	 * 
	 * @param iResult
	 *            the RPC call result to set
	 */
	public void setResult( Integer iResult );

	/**
	 * Returns primary key value
	 * 
	 * @return the primary key value or null
	 */
	public String getPKValue( );

	/**
	 * Returns error message
	 * 
	 * @return the error message
	 */
	public String getError( );
	
	/**
	 * Sets error message
	 * 
	 * @param sError
	 *            the error message to set
	 */
	public void setError( String sError );

	/**
	 * Returns data row count
	 * 
	 * @return the row count
	 */
	public Integer getRowCount( );

	/**
	 * Sets data row count
	 * 
	 * @param iRowCount
	 *            the row count to set
	 */
	public void setRowCount( Integer iRowCount );

	/**
	 * Returns identification object
	 * 
	 * @return the identification object
	 */
	public Id getID( );

	/**
	 * Sets identification object
	 * 
	 * @param id
	 *            the identification object
	 */
	public void setID( Id id );

	/**
	 * Returns page number (if server paging is on)
	 * 
	 * @return the page number
	 */
	public Integer getPageNum( );

	/**
	 * Sets page number (if server paging is on)
	 * 
	 * @param iPageNum the page number to set
	 */
	public void setPageNum( Integer iPageNum );

	/**
	 * Returns server paging flag
	 * 
	 * @return the server paging flag
	 */
	public Boolean isServerPaging( );

	/**
	 * Sets server paging flag
	 * 
	 * @param bServerPaging
	 *            the server paging flag value to set
	 */
	public void setServerPaging( Boolean bServerPaging );

	/**
	 * Returns downloading file name
	 * 
	 * @return the downloading file name
	 */
	public String getDownloadFileName( );
	
	/**
	 * Sets downloading file name
	 * 
	 * @param sFile
	 *            the downloading file name to set
	 */
	public void setDownloadFileName( String sFile );

	/**
	 * Returns dbms message
	 * 
	 * @return the message or null
	 */
	public String getMsg( );

	/**
	 * Sets return value
	 * 
	 * @param rv the return value to set
	 */
	public void setReturnValue( ReturnValue rv );
}
