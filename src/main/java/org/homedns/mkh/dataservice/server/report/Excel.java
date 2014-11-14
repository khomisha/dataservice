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

package org.homedns.mkh.dataservice.server.report;

import jxl.CellType;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.homedns.mkh.databuffer.Column;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.InvalidDatabufferDesc;
import org.homedns.mkh.databuffer.Type;
import jxl.write.DateTime;
import jxl.write.Number;

/**
 * Creates excel files using prepared templates, inserts retrieved from
 * database data using data buffer
 * 
 */
public class Excel {
	private String _sTemplate;
	private DataBuffer _db;
	
	/**
	 * Excel constructor.
	 * 
	 * @param sTemplate
	 *            excel template file full name
	 * @param db
	 *            data buffer
	 */
	public Excel( String sTemplate, DataBuffer db ) {
		_sTemplate = sTemplate;
		_db = db;
	}
	
	/**
	 * Returns excel file filled with report data
	 * 
	 * @return excel full file name
	 * 
	 * @throws IOException
	 * @throws WriteException
	 * @throws BiffException
	 * @throws SQLException
	 * @throws ParseException
	 * @throws InvalidDatabufferDesc 
	 */
	public String getExcelFile( ) 
		throws IOException, WriteException, BiffException, 
		SQLException, ParseException, InvalidDatabufferDesc {
		return( getExcelData( ) );
	}

	/**
	 * Generates excel file based on given template, retrieves data from
	 * database using data buffer, inserts retrieved data to the excel file
	 * 
	 * @return excel full file name
	 * 
	 * @throws IOException
	 * @throws WriteException
	 * @throws BiffException
	 * @throws RowsExceededException
	 * @throws SQLException
	 * @throws ParseException
	 * @throws InvalidDatabufferDesc
	 */
	protected String getExcelData( ) 
		throws IOException, WriteException, 
		BiffException, RowsExceededException, 
		SQLException, ParseException, InvalidDatabufferDesc 
	{
		String sReport = _sTemplate.substring( _sTemplate.lastIndexOf( "/" ) ); 
		sReport = System.getProperty( "user.dir" ) + "/reports/" + sReport.replaceFirst( 
			"template.xls", String.valueOf( ( new Date( ) ).getTime( ) ) + ".xls" 
		);
		WritableWorkbook workBook = null;
		try {
			workBook = Workbook.createWorkbook( 
				new File( sReport ), 
				Workbook.getWorkbook( new File( _sTemplate ) ) 
			);
			WritableSheet sheet = workBook.getSheet( 0 );
			
			List< Column > listCol = new ArrayList< Column >( );
			String sReportParam;
			for( Column col : _db.getDescription( ).getColumns( ) ) {
				sReportParam = col.getReportParam( );
				if( "".equals( sReportParam ) || sReportParam == null ) {
					if( !"".equals( col.getStyle( ) ) ) {
						listCol.add( col );
					}
				} else {
					insertParam( sheet, sReportParam, col );
				}
			}
			String sReportData = _db.getDescription( ).getTable( ).getReportData( );
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
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws SQLException
	 * @throws ParseException
	 */
	private void insertData(
		WritableSheet sheet,
		String sReportData, 
		List< Column > listCol 
	) throws RowsExceededException, WriteException, SQLException, ParseException {
		String[][] asData = _db.getData( listCol );
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
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws SQLException
	 * @throws InvalidDatabufferDesc 
	 */
	private void insertParam( 
		WritableSheet sheet,
		String sReportParam, 
		Column col 
	) throws RowsExceededException, WriteException, SQLException, InvalidDatabufferDesc {
		int[] aiColRow = getCellIndex( sReportParam );
	    int iColNum = col.getColNum( );
		_db.beforeFirst( );
		if( _db.next( ) ) {
		    WritableCell cell = sheet.getWritableCell( aiColRow[ 0 ], aiColRow[ 1 ] );
		    if( cell.getType( ) == CellType.LABEL ) {
		    	( ( Label )cell ).setString( _db.getString( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.BOOLEAN ) {
		    	( ( jxl.write.Boolean )cell ).setValue( _db.getBoolean( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.DATE ) {
		    	( ( DateTime )cell ).setDate( _db.getDate( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.NUMBER ) {
		    	( ( Number )cell ).setValue( _db.getDouble( iColNum + 1 ) );
		    } else if( cell.getType( ) == CellType.EMPTY ) {
		    	CellFormat fmt = cell.getCellFormat( );
		    	Type type = col.getType( );
				if( type == Type.STRING ) {
					sheet.addCell( 
						new Label( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							_db.getString( iColNum + 1 ), 
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
							_db.getDouble( iColNum + 1 ), 
							fmt 
						) 
					);
				} else if( type == Type.BOOLEAN ) {
					sheet.addCell( 
						new jxl.write.Boolean( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							_db.getBoolean( iColNum + 1 ), fmt 
						) 
					);
				} else if( type == Type.TIMESTAMP ) {
					sheet.addCell( 
						new DateTime( 
							aiColRow[ 0 ], 
							aiColRow[ 1 ], 
							_db.getDate( iColNum + 1 ), 
							fmt 
						) 
					);
				}
		    } else {
		    	sheet.addCell( 
		    		new Label( 
		    			aiColRow[ 0 ], 
		    			aiColRow[ 1 ], 
		    			_db.getString( iColNum + 1 ) 
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
