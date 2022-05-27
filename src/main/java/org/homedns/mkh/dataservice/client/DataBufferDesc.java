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

package org.homedns.mkh.dataservice.client;

import java.util.Arrays;
import java.util.List;
import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.core.client.GWT;

/**
 * @see org.homedns.mkh.databuffer.DataBufferDesc
 */
public class DataBufferDesc {	
	public interface DataBufferDescDecoder extends JsonEncoderDecoder< DataBufferDesc > {}
    public static final DataBufferDescDecoder DECODER = GWT.create( DataBufferDescDecoder.class );

	private String name;
	private String title;
	private Table table;
	private Column[] columns;
	private String[] colNames;
	
	public DataBufferDesc( ) {
	}
	
	/**
	 * Returns data buffer name
	 * 
	 * @return the data buffer name
	 */
	public String getName( ) {
		return( name );
	}
	
	/**
	 * Sets data buffer name
	 * 
	 * @param name
	 *            the data buffer name to set
	 */
	public void setName( String name ) {
		this.name = name;
	}
	
	/**
	 * Returns data buffer title
	 * 
	 * @return the data buffer title
	 */
	public String getTitle( ) {
		return( title );
	}
	
	/**
	 * Sets data buffer title
	 * 
	 * @param title
	 *            the data buffer title to set
	 */
	public void setTitle( String title ) {
		this.title = title;
	}
	
	/**
	 * Returns data buffer table
	 * 
	 * @return the data buffer table
	 */
	public Table getTable( ) {
		return( table );
	}
	
	/**
	 * Sets data buffer table
	 * 
	 * @param table
	 *            the data buffer table to set
	 */
	public void setTable( Table table ) {
		this.table = table;
	}
	
	/**
	 * Returns data buffer columns
	 * 
	 * @return the data buffer columns
	 */
	public Column[] getColumns( ) {
		return( columns );
	}
	
	/**
	 * Sets data buffer columns
	 * 
	 * @param columns the data buffer columns to set
	 */
	public void setColumns( Column[] columns ) {
		this.columns = columns;
	}

	/**
	 * Returns column by it's name
	 * 
	 * @param sColName
	 *            the column name
	 * 
	 * @return the column
	 */
	public Column getColumn( String sColName ) {
		return( this.columns[ getColIndex( sColName ) ] );
	}
	
	/**
	 * Returns column index by it's name
	 * 
	 * @param sColName the column name
	 * 
	 * @return the column index
	 */
	public int getColIndex( String sColName ) {
		List< String > colList = Arrays.asList( colNames );
		int iIndex = colList.indexOf( sColName );
		if( iIndex == -1 ) {
			throw new IllegalArgumentException( "no " + sColName );
		}
		return( iIndex );
	}

	/**
	 * Returns column names array
	 * 
	 * @return the column names array
	 */
	public String[] getColNames( ) {
		return colNames;
	}

	/**
	 * Sets column names array
	 * 
	 * @param colsNames
	 *            the column names array to set
	 */
	public void setColNames( String[] colNames ) {
		this.colNames = colNames;
	}
}

