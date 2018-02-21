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
 * Data buffer column object
 *
 */
public class Column {
	public interface ColumnDecoder extends JsonEncoderDecoder< Column > {}
    public static final ColumnDecoder DECODER = GWT.create( ColumnDecoder.class );

	/**
	* Dropdown listbox style
	*/
	public static final String DDLB			= "ddlb";
	/**
	* Dropdown data buffer style
	 */
	public static final String DDDB			= "dddb";
	/**
	 * Checkbox style
	 */
	public static final String CHECKBOX		= "checkbox";
	/**
	 * Radiobutton style
	 */
	public static final String RADIOBUTTON	= "radiobutton";
	/**
	 * General purpose edit style
	 */
	public static final String EDIT			= "edit";
	/**
	 * Password style
	 */
	public static final String PWD			= "pwd";
	/**
	 * Date style
	 */
	public static final String EDIT_DATE	= "edit_date";
	/**
	 * Time style
	 */
	public static final String EDIT_TIME	= "edit_time";
	/**
	 * Timestamp style
	 */
	public static final String EDIT_TS		= "edit_ts";

	private String name;
	private String caption;
	private String dbName;
	private String type;
	private boolean update;
	private String validationRule;
	private String validationMsg;
	private String style;
	private int limit;
	private boolean required;
	private String mask;
	private Value[] values;
	private String dddbName;
	private String dddbDisplayCol;
	private String dddbDataCol;
	private String reportParam = "";
	private int colNum;
	private String pattern = "";
	
	public Column( ) {
	}
	
	/**
	 * Returns column name
	 * 
	 * @return the column name
	 */
	public String getName( ) {
		return( name );
	}
	
	/**
	 * Sets column name
	 * 
	 * @param name
	 *            the column name to set
	 */
	public void setName( String name ) {
		this.name = name;
	}
		
	/**
	 * Returns column caption
	 * 
	 * @return the column caption
	 */
	public String getCaption( ) {
		return( caption );
	}
	
	/**
	 * Sets column caption
	 * 
	 * @param caption
	 *            the column caption to set
	 */
	public void setCaption( String caption ) {
		this.caption = caption;
	}
	
	/**
	 * Returns column database name
	 * 
	 * @return the column database name
	 */
	public String getDbName( ) {
		return( dbName );
	}
	
	/**
	 * Sets column database name
	 * 
	 * @param dbName the column database name to set
	 */
	public void setDbName( String dbName ) {
		this.dbName = dbName;
	}
	
	/**
	 * Returns column type
	 * 
	 * @return the column type
	 */
	public Type getColType( ) {
		return( Type.valueOf( type ) );
	}
	
	/**
	 * Returns column type
	 * 
	 * @return the column type
	 */
	public String getType( ) {
		return( type );
	}
	
	/**
	 * Sets column type
	 * 
	 * @param type the column type to set
	 */
	public void setType( String type ) {
		this.type = type;
	}
	
	/**
	 * Returns column update flag
	 * 
	 * @return the column update flag
	 */
	public boolean isUpdate( ) {
		return( update );
	}
	
	/**
	 * Sets column update flag
	 * 
	 * @param update the column update flag to set
	 */
	public void setUpdate( boolean update ) {
		this.update = update;
	}
	
	/**
	 * Returns column validation rule (regex expression)
	 * 
	 * @return the column validation rule 
	 */
	public String getValidationRule( ) {
		return( validationRule );
	}
	
	/**
	 * Sets column validation rule
	 * 
	 * @param validationRule the column validation rule to set
	 */
	public void setValidationRule( String validationRule ) {
		this.validationRule = validationRule;
	}
	
	/**
	 * Returns validation message
	 * 
	 * @return the validation message
	 */
	public String getValidationMsg( ) {
		return( validationMsg );
	}
	
	/**
	 * Sets validation message
	 * 
	 * @param validationMsg the validation message to set
	 */
	public void setValidationMsg( String validationMsg ) {
		this.validationMsg = validationMsg;
	}
	
	/**
	 * Returns column style
	 * 
	 * @return the column style
	 */
	public String getStyle( ) {
		return( style );
	}
	
	/**
	 * Sets column style
	 * 
	 * @param style the column style to set
	 */
	public void setStyle( String style ) {
		this.style = style;
	}
	
	/**
	 * Returns column length in bytes, 0 means non dimensional
	 * 
	 * @return the column length in bytes
	 */
	public int getLimit( ) {
		return( limit );
	}
	
	/**
	 * Sets column length in bytes
	 * 
	 * @param limit the column length to set
	 */
	public void setLimit( int limit ) {
		this.limit = limit;
	}
	
	/**
	 * Returns column mandatory flag
	 * 
	 * @return the column mandatory flag
	 */
	public boolean isRequired( ) {
		return( required );
	}
	
	/**
	 * Sets column mandatory flag
	 * 
	 * @param required the column mandatory flag to set
	 */
	public void setRequired( boolean required ) {
		this.required = required;
	}
	
	/**
	 * Returns column mask, the keystroke filter mask to be applied to the column
	 * input value type (regex expression)
	 * 
	 * @return the column mask
	 */
	public String getMask( ) {
		return( mask );
	}
	
	/**
	 * Sets column mask, the keystroke filter mask to be applied to the column
	 * input value type (regex expression)
	 * 
	 * @param mask the column mask to set
	 */
	public void setMask( String mask ) {
		this.mask = mask;
	}
	
	/**
	 * Returns array which contains pairs display value - actual value
	 * 
	 * @return the values array
	 */
	public Value[] getValues( ) {
		return( values );
	}
	
	/**
	 * Sets array which contains pairs display value - actual value
	 * 
	 * @param values the values array to set
	 */
	public void setValues( Value[] values ) {
		this.values = values;
	}
	
	/**
	 * Returns dropdown databuffer name
	 * 
	 * @return the dropdown databuffer name
	 */
	public String getDddbName( ) {
		return( dddbName );
	}
	
	/**
	 * Sets dropdown databuffer name
	 * 
	 * @param dddbName the dropdown databuffer name to set
	 */
	public void setDddbName( String dddbName ) {
		this.dddbName = dddbName;
	}
	
	/**
	 * Returns dropdown data buffer display column name
	 * 
	 * @return the dropdown data buffer display column name
	 */
	public String getDddbDisplayCol( ) {
		return( dddbDisplayCol );
	}
	
	/**
	 * Sets dropdown data buffer display column name
	 * 
	 * @param dddbDisplayCol
	 *            the dropdown data buffer display column name to set
	 */
	public void setDddbDisplayCol( String dddbDisplayCol ) {
		this.dddbDisplayCol = dddbDisplayCol;
	}
	
	/**
	 * Returns dropdown data buffer data column name
	 * 
	 * @return the dropdown data buffer data column name
	 */
	public String getDddbDataCol( ) {
		return( dddbDataCol );
	}
	
	/**
	 * Sets dropdown data buffer data column name
	 * 
	 * @param dddbDataCol
	 *            the dropdown data buffer data column name to set
	 */
	public void setDddbDataCol( String dddbDataCol ) {
		this.dddbDataCol = dddbDataCol;
	}
	
	/**
	 * Returns report indicates cell (column,row) in excel sheet template
	 * 
	 * @return the indicates cell
	 */
	public String getReportParam( ) {
		return( reportParam );
	}
	
	/**
	 * Sets indicates cell (column,row) in excel sheet template
	 * 
	 * @param reportParam
	 *            the cell (column,row) to set
	 */
	public void setReportParam( String reportParam ) {
		this.reportParam = reportParam;
	}
	
	/**
	 * Returns column number
	 * 
	 * @return the column number
	 */
	public int getColNum( ) {
		return( colNum );
	}
	
	/**
	 * Sets column number
	 * 
	 * @param colNum the column number to set
	 */
	public void setColNum( int colNum ) {
		this.colNum = colNum;
	}
	
	/**
	 * Returns column format pattern
	 * 
	 * @return the column format pattern
	 */
	public String getPattern( ) {
		return( pattern );
	}

	/**
	 * Sets column format pattern
	 * 
	 * @param pattern the column format pattern to set
	 */
	public void setPattern( String pattern ) {
		this.pattern = pattern;
	}	
}
