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

package org.homedns.mkh.dataservice.client;

/**
 * Page object encapsulates paging navigation
 *
 */
public class Page {
	private int _iPageSize;
	private int _iRowCount;
	private int _iPage = 1;
	private boolean _bChanged;

	public Page( ) {
	}
	
	/**
	 * @param iPageSize
	 *            the page size
	 * @param iRowCount
	 *            the row count
	 */
	public Page( int iPageSize, int iRowCount ) {
		_iPageSize = iPageSize;
		_iRowCount = iRowCount;
	}

	/**
	 * Returns current page number
	 * 
	 * @return the current page number
	 */
	public int getPageNumber( ) {
		return( _iPage );
	}
	
	/**
	 * Increases current page number, if current page + 1 > last page number
	 * then set current page number = last page number
	 * 
	 * @return this
	 */
	public Page nextPage( ) {
		int iPage = check( _iPage + 1 );
		_bChanged = ( _iPage != iPage );
		_iPage = iPage;
		return( this );
	}

	/**
	 * Decreases current page number, if current page - 1 < 1 then set current
	 * page number = 1
	 * 
	 * @return this
	 */
	public Page prevPage( ) {
		int iPage = check( _iPage - 1 );		
		_bChanged = ( _iPage != iPage );
		_iPage = iPage;
		return( this );
	}
	
	/**
	 * Sets current page number to 1
	 * 
	 * @return this
	 */
	public Page firstPage( ) {
		int iPage = 1;
		_bChanged = ( _iPage != iPage );
		_iPage = iPage;
		return( this );
	}
	
	/**
	 * Sets current page number to last page number
	 * 
	 * @return this
	 */
	public Page lastPage( ) {
		int iPage = getLastPage( );
		_bChanged = ( _iPage != iPage );
		_iPage = iPage;
		return( this );
	}
	
	/**
	 * Sets current page number
	 * 
	 * @param iPage the page number to set
	 */
	public void setPageNumber( int iPage ) {
		iPage = check( iPage );
		_bChanged = ( _iPage != iPage );
		_iPage = iPage;
	}

	/**
	 * Returns page first row number
	 * 
	 * @return the page first row number
	 */
	public int getFirstRow( ) {
		return( ( _iPage - 1 ) * _iPageSize );
	}
	
	/**
	 * Returns page last row number
	 * 
	 * @return the page last row number
	 */
	public int getLastRow( ) {
		return( getFirstRow( ) + _iPageSize );
	}
	
	/**
	 * Returns page size
	 * 
	 * @return the page size
	 */
	public int getPageSize( ) {
		return( _iPageSize );
	}

	/**
	 * Returns row count
	 * 
	 * @return the row count
	 */
	public int getRowCount( ) {
		return( _iRowCount );
	}

	/**
	 * Sets page size
	 * 
	 * @param iPageSize the page size to set
	 */
	public void setPageSize( int iPageSize ) {
		_iPageSize = iPageSize;
	}

	/**
	 * Sets row count 
	 * 
	 * @param iRowCount the row count to set
	 */
	public void setRowCount( int iRowCount ) {
		_iRowCount = iRowCount;
	}

	/**
	 * Returns true if page number is changed and false otherwise
	 * 
	 * @return the page number changed flag
	 */
	public boolean isChanged( ) {
		return( _bChanged );
	}

	/**
	 * Returns last page number
	 * 
	 * @return the last page number
	 */
	public int getLastPage( ) {
		return( 
			_iRowCount % _iPageSize > 0 ? _iRowCount / _iPageSize + 1 : _iRowCount / _iPageSize 
		);
	}

	/**
	 * Checks page number limits, if iPage < 1 then will return 1, if iPage >
	 * last page number then will return last page number otherwise will return
	 * input page number
	 * 
	 * @param iPage
	 *            the input page number
	 * 
	 * @return the valid page number
	 */
	private int check( int iPage ) {
		return( iPage < 1 ? 1 : iPage > getLastPage( ) ? getLastPage( ) : iPage );
	}
}
