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

package org.homedns.mkh.dataservice.shared;

import java.io.Serializable;

/**
 * Generic RPC call response object
 *
 */
public class GenericResponse implements Response {
	private static final long serialVersionUID = 5632380352454748073L;
	
	private static final int DATA 				= 6000;
	private static final int DATABUFFER_DESC	= 6001;
	private static final int RETURN_VALUE		= 6002;
	private static final int DOWNLOAD_FILENAME	= 6003;
	private static final int PAGE_NUM			= 6005;
	private static final int RESULT 			= 6006;
	private static final int ERROR_MSG 			= 6007;
	private static final int SERVER_PAGING		= 6008;
	private static final int ID 				= 6009;	
	private static final int ROW_COUNT			= 6010;	

	@SuppressWarnings( "unused" )
	private String[][] dummy;
	private AttributeMap< Integer, Serializable > _attributes = (
		new AttributeMap< Integer, Serializable >( )
	);

	public GenericResponse( ) {
		setServerPaging( false );
		setRowCount( 0 );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getData()
	 */
	public String[][] getData( ) {
		return( getAttribute( String[][].class, DATA ) );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setData(java.lang.String[][])
	 */
	public void setData( String[][] asData ) {
		setAttribute( DATA, asData );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getJsonData()
	 */
	public String getJsonData( ) {
		return( getAttribute( String.class, DATA ) );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setJsonData(java.lang.String)
	 */
	public void setJsonData( String sData ) {
		setAttribute( DATA, sData );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getDataBufferDesc()
	 */
	public String getDataBufferDesc( ) {
		return( getAttribute( String.class, DATABUFFER_DESC ) );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setDataBufferDesc(java.lang.String)
	 */
	public void setDataBufferDesc( String sDataBufferDesc ) {
		setAttribute( DATABUFFER_DESC, sDataBufferDesc );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getResult()
	 */
	public Integer getResult( ) {
		return( getAttribute( Integer.class, RESULT ) );		
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setResult(java.lang.Integer)
	 */
	public void setResult( Integer iResult ) {
		setAttribute( RESULT, iResult );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getPKValue()
	 */
	public String getPKValue( ) {
		String sValue = null;
		ReturnValue rv = getAttribute( ReturnValue.class, RETURN_VALUE );
		if( rv != null && !rv.isEmpty( ) ) {
			sValue = rv.get( 0 );
		}
		return( sValue );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getError()
	 */
	public String getError( ) {
		return( getAttribute( String.class, ERROR_MSG ) );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setError(java.lang.String)
	 */
	public void setError( String sError ) {
		setAttribute( ERROR_MSG, sError );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getReturnValueAsString(int)
	 */
	public String getReturnValueAsString(int iIndex ) {
		String sValue = null;
		ReturnValue rv = getAttribute( ReturnValue.class, RETURN_VALUE );
		if( rv != null && iIndex < rv.size( ) ) {
			sValue = rv.get( iIndex );
		}
		return( sValue );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setReturnValue(org.homedns.mkh.dataservice.shared.ReturnValue)
	 */
	public void setReturnValue( ReturnValue rv ) {
		setAttribute( RETURN_VALUE, rv );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getRowCount()
	 */
	public Integer getRowCount( ) {
		return( getAttribute( Integer.class, ROW_COUNT ) );		
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setRowCount(java.lang.Integer)
	 */
	public void setRowCount( Integer iRowCount ) {
		setAttribute( ROW_COUNT, iRowCount );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getID()
	 */
	public Id getID( ) {
		return( getAttribute( Id.class, ID ) );		
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setID(org.homedns.mkh.dataservice.shared.Id)
	 */
	public void setID( Id id ) {
		setAttribute( ID, id );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getPageNum()
	 */
	public Integer getPageNum( ) {
		return( getAttribute( Integer.class, PAGE_NUM ) );		
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setPageNum(java.lang.Integer)
	 */
	public void setPageNum( Integer iPageNum ) {
		setAttribute( PAGE_NUM, iPageNum );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#isServerPaging()
	 */
	public Boolean isServerPaging( ) {
		return( getAttribute( Boolean.class, SERVER_PAGING ) );		
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setServerPaging(java.lang.Boolean)
	 */
	public void setServerPaging( Boolean bServerPaging ) {
		setAttribute( SERVER_PAGING, bServerPaging );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#getDownloadFileName()
	 */
	public String getDownloadFileName( ) {
		return( getAttribute( String.class, DOWNLOAD_FILENAME ) );
	}
	
	/**
	 * @see org.homedns.mkh.dataservice.shared.Response#setDownloadFileName(java.lang.String)
	 */
	public void setDownloadFileName( String sFile ) {
		setAttribute( DOWNLOAD_FILENAME, sFile );
	}

	/**
	* Returns attribute value.
	*
	* @param type the expected attribute value type
	* @param iKey the attribute key
	*
	* @return attribute value or null
	*/
	@SuppressWarnings("unchecked")
	protected < T extends Serializable > T getAttribute( Class< T > type, Integer iKey ) {
		Serializable value = _attributes.getAttribute( iKey );
		if( value == null ) {
			return( null );
		}
		if( type == value.getClass( ) ) {
			return( ( T )value );
		} else {
			throw new IllegalArgumentException( type.getName( ) );
		}
	}
	
	/**
	 * Sets attribute value
	 * 
	 * @param iKey the attribute key
	 * @param value the attribute value
	 */
	protected void setAttribute( Integer iKey, Serializable value ) {
		_attributes.setAttribute( iKey, value );
	}
}
