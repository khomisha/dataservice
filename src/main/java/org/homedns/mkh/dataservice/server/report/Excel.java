/*
* Copyright 2013-2022 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.server.report;

import jxl.CellType;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.rowset.WebRowSet;
import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.Column;
import org.homedns.mkh.databuffer.Type;
import org.homedns.mkh.databuffer.api.DataBuffer;
import org.homedns.mkh.dataservice.server.Context;
import jxl.write.DateTime;
import jxl.write.Number;

/**
 * Creates excel files using prepared templates, inserts retrieved from
 * database data using data buffer
 * 
 */
public class Excel {
	private static final Logger LOG = Logger.getLogger( Excel.class );

	private String sTemplate;
	private DataBuffer db;
	
	/**
	 * @param sTemplate
	 *            excel template file full name
	 * @param db
	 *            data buffer
	 */
	public Excel( String sTemplate, DataBuffer db ) {
		this.sTemplate = sTemplate;
		this.db = db;
	}
	
	/**
	 * Returns excel file filled with report data
	 * 
	 * @return excel full file name
	 * 
	 * @throws Exception
	 */
	public String getExcelFile( ) throws Exception {
		return( getExcelData( ) );
	}

	/**
	 * Generates excel file based on given template, retrieves data from
	 * database using data buffer, inserts retrieved data to the excel file
	 * 
	 * @return excel full file name
	 * 
	 * @throws Exception
	 */
	protected String getExcelData( ) throws Exception {
		String sReport = sTemplate.substring( sTemplate.lastIndexOf( "/" ) ); 
		sReport = (
			Context.getInstance( ).getServletContext( ).getResource( "/" ).getPath( ) + 
			"reports" + 
			sReport.replaceFirst( "template.xls", String.valueOf( ( new Date( ) ).getTime( ) ) + ".xls" )
		);
		LOG.debug( "report filename: " + sReport );
		LOG.debug( "template filename: " + sTemplate );
		WritableWorkbook workBook = null;
		try {
			File report = new File( sReport );
			File template = new File( sTemplate );
			workBook = Workbook.createWorkbook( report, Workbook.getWorkbook( template ) );
			WritableSheet sheet = workBook.getSheet( 0 );
			
			List< Column > listCol = new ArrayList< Column >( );
			String sReportParam;
			for( Column col : db.getDescription( ).getColumns( ) ) {
				sReportParam = col.getReportParam( );
				if( "".equals( sReportParam ) || sReportParam == null ) {
					if( !"".equals( col.getStyle( ) ) ) {
						listCol.add( col );
					}
				} else {
					insertParam( sheet, sReportParam, col );
				}
			}
			String sReportData = db.getDescription( ).getTable( ).getReportData( );
			insertData( sheet, sReportData, listCol );
			workBook.write( );
		}
		finally {
			if( workBook != null ) {
				workBook.close( );
			}
		}
		return( sReport );
	}
	
	/**
	 * Inserts data to the excel sheet.
	 * 
	 * @param sheet the destination sheet
	 * @param sReportData
	 *            start column and row indexes in excel sheet template where
	 *            data buffer data should be inserted
	 * @param listCol
	 *            list of the selected columns which data will be inserted
	 * 
	 * @throws Exception
	 */
	private void insertData( WritableSheet sheet, String sReportData, List< Column > listCol ) throws Exception {
		String[][] asData = db.getData( listCol );
		int[] aiColRow = getCellIndex( sReportData );
		int iSheetRow = aiColRow[ 1 ];
		for( int iRow = 0; iRow < asData.length; iRow++ ) {
			sheet.insertRow( iSheetRow );
			int iSheetCol = aiColRow[ 0 ];
			for( int iCol = 0; iCol < asData[ iRow ].length; iCol++ ) {
				sheet.addCell( new Label( iSheetCol, iSheetRow, asData[ iRow ][ iCol ] ) );
				iSheetCol++;
			}
			iSheetRow++;
		}
	}
	
	/**
	 * Inserts params to the excel sheet.
	 * 
	 * @param sheet the destination sheet
	 * @param sReportParam
	 *            cell indexes (column, row) in excel sheet template where
	 *            parameter value should be inserted
	 * @param col
	 *            data buffer column
	 * 
	 * @throws Exception
	 */
	private void insertParam( WritableSheet sheet, String sReportParam, Column col ) throws Exception {
		int[] aiColRow = getCellIndex( sReportParam );
	    int iColNum = col.getColNum( );
	    WebRowSet wrs = db.getParent( );
	    wrs.beforeFirst( );
		if( wrs.next( ) ) {
		    WritableCell cell = sheet.getWritableCell( aiColRow[ 0 ], aiColRow[ 1 ] );
		    if( cell.getType( ) == CellType.LABEL ) {
		    	( ( Label )cell ).setString( wrs.getString( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.BOOLEAN ) {
		    	( ( jxl.write.Boolean )cell ).setValue( wrs.getBoolean( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.DATE ) {
		    	( ( DateTime )cell ).setDate( wrs.getDate( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.NUMBER ) {
		    	( ( Number )cell ).setValue( wrs.getDouble( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.EMPTY ) {
		    	CellFormat fmt = cell.getCellFormat( );
		    	Type type = col.getType( );
				if( type == Type.STRING ) {
					sheet.addCell( 
						new Label( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							wrs.getString( iColNum + 1 ), 
							fmt 
						) 
					);
				} else if( 
					type == Type.INT || 
					type == Type.SHORT || 
					type == Type.LONG || 
					type == Type.BYTE ||
					type == Type.DOUBLE ||
					type == Type.FLOAT
				) {
					sheet.addCell( 
						new Number( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							wrs.getDouble( iColNum + 1 ), 
							fmt 
						) 
					);
				} else if( type == Type.BOOLEAN ) {
					sheet.addCell( 
						new jxl.write.Boolean( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							wrs.getBoolean( iColNum + 1 ), fmt 
						) 
					);
				} else if( type == Type.TIMESTAMP ) {
					sheet.addCell( 
						new DateTime( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							wrs.getDate( iColNum + 1 ), 
							fmt 
						) 
					);
				}
		    } else {
		    	sheet.addCell( 
		    		new Label( 
		    			aiColRow[ 0 ], 
		    			aiColRow[ 1 ], 
		    			wrs.getString( iColNum + 1 ) 
		    		) 
		    	);
		    }
		}
	}
	
	/**
	* Returns cell indexes (column, row) where data should be inserted in excel sheet.
	*
	* @param sCellIndex cell indexes as string getting from data buffer description
	* 
	* @return column and row indexes array
	*/
	private int[] getCellIndex( String sCellIndex ) {
		String[] as = sCellIndex.split( "," );
		return( new int[] { Integer.valueOf( as[ 0 ] ), Integer.valueOf( as[ 1 ] ) } );
	}
}
