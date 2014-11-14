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

import org.homedns.mkh.dataservice.client.sender.RPCRequestSender;

/**
 * Generic request. Default sender type {@link org.homedns.mkh.dataservice.client.sender.RPCRequestSender}
 * 
 */
@SuppressWarnings( "serial" )
public class GenericRequest implements Request {

	private Id _id;
	private String _sHandlerClassName;
	private String _sResponseClassName;
	private boolean _bInitPresenter = true;
	private String _sSenderClassName;

	public GenericRequest( ) { 
		setSenderType( RPCRequestSender.class.getName( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		inputRequest.setHandlerClassName( _sHandlerClassName );
		inputRequest.setID( _id );
		inputRequest.setInitPresenter( _bInitPresenter );
		inputRequest.setResponseClassName( _sResponseClassName );
		inputRequest.setSenderType( _sSenderClassName );
		return( inputRequest );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setID(org.homedns.mkh.dataservice.shared.Id)
	 */
	@Override
	public void setID( Id id ) {
		_id = id;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getID()
	 */
	@Override
	public Id getID( ) {
		return( _id );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getHandlerClassName()
	 */
	@Override
	public String getHandlerClassName( ) {
		return( _sHandlerClassName );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setHandlerClassName(java.lang.String)
	 */
	@Override
	public void setHandlerClassName( String sHandlerClassName ) {
		_sHandlerClassName = sHandlerClassName;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#isInitPresenter()
	 */
	@Override
	public boolean isInitPresenter( ) {
		return( _bInitPresenter );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setInitPresenter(boolean)
	 */
	@Override
	public void setInitPresenter( boolean bInitPresenter ) {
		_bInitPresenter = bInitPresenter;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getSenderType()
	 */
	@Override
	public String getSenderType( ) {
		return( _sSenderClassName );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setSenderType(java.lang.String)
	 */
	@Override
	public void setSenderType( String sSenderClassName ) {
		_sSenderClassName = sSenderClassName;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getResponseClassName()
	 */
	@Override
	public String getResponseClassName( ) {
		return( _sResponseClassName );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setResponseClassName(java.lang.String)
	 */
	@Override
	public void setResponseClassName( String sResponseClassName ) {
		_sResponseClassName = sResponseClassName;
	}
}
