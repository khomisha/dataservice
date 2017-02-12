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

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.core.client.GWT;

/**
 * Data buffer table
 *
 */
public class Table {
	public interface TableDecoder extends JsonEncoderDecoder< Table > {};
    public static final TableDecoder DECODER = GWT.create( TableDecoder.class );

	private String updateTableName;
	private String select;
	private String key;
	private String rowCountColumn;
	private String[] argType;
	private String reportData;
	private int pageSize = 0;

	public Table( ) {
	}
	
	/**
	 * Returns update table name
	 * 
	 * @return the update table name
	 */
	public String getUpdateTableName( ) {
		return( updateTableName );
	}
	
	/**
	 * Sets update table name
	 * 
	 * @param updateTableName
	 *            the update table name to set
	 */
	public void setUpdateTableName( String updateTableName ) {
		this.updateTableName = updateTableName;
	}
	
	/**
	 * Returns data buffer retrieve query
	 * 
	 * @return the data buffer retrieve query
	 */
	public String getSelect( ) {
		return( select );
	}
	
	/**
	 * Sets data buffer retrieve query
	 * 
	 * @param select
	 *            the data buffer retrieve query to set
	 */
	public void setSelect( String select ) {
		this.select = select;
	}
	
	/**
	 * Returns primary key column name
	 * 
	 * @return the primary key column name 
	 */
	public String getKey( ) {
		return( key );
	}
	
	/**
	 * Sets primary key column name
	 * 
	 * @param key
	 *            the primary key column name to set
	 */
	public void setKey( String key ) {
		this.key = key;
	}
	
	/**
	 * Returns row count column name
	 * 
	 * @return the row count column name
	 */
	public String getRowCountCol( ) {
		return( rowCountColumn );
	}
	
	/**
	 * Sets row count column name
	 * 
	 * @param rowCountColumn
	 *            the row count column name to set
	 */
	public void setRowCountCol( String rowCountColumn ) {
		this.rowCountColumn = rowCountColumn;
	}
	
	/**
	 * Returns data buffer query arguments data types array
	 * 
	 * @return the data buffer query arguments types array 
	 */
	public String[] getArgType( ) {
		return( argType );
	}
	
	/**
	 * Sets data buffer query arguments data types array
	 * 
	 * @param argType
	 *            the data buffer query arguments data types array to set
	 */
	public void setArgType( String[] argType ) {
		this.argType = argType;
	}
	
	/**
	 * Returns report data anchor point
	 * 
	 * @return the report data anchor point
	 */
	public String getReportData( ) {
		return( reportData );
	}
	
	/**
	 * Sets report data anchor point
	 * 
	 * @param reportData
	 *            the report data anchor point to set
	 */
	public void setReportData( String reportData ) {
		this.reportData = reportData;
	}

	/**
	 * Returns page size
	 * 
	 * @return the page size
	 */
	public int getPageSize( ) {
		return( pageSize );
	}

	/**
	 * Sets page size
	 * 
	 * @param pageSize
	 *            the page size to set
	 */
	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
	}
	
	/**
	 * Returns true if server paging on and false otherwise
	 * 
	 * @return the server paging flag
	 */
	public boolean isServerPaging( ) {
		return( pageSize > 0 && rowCountColumn != null && !"".equals( rowCountColumn ) );
	}
}
