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

	private Id id;
	private String sHandlerClassName;
	private String sResponseClassName;
	private boolean bInitPresenter = true;
	private String sSenderClassName;
	private boolean bBatch;

	public GenericRequest( ) { 
		setBatchUpdate( false );
		setSenderType( RPCRequestSender.class.getName( ) );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#copy(org.homedns.mkh.dataservice.shared.Request)
	 */
	@Override
	public Request copy( Request inputRequest ) {
		inputRequest.setHandlerClassName( sHandlerClassName );
		inputRequest.setID( id );
		inputRequest.setInitPresenter( bInitPresenter );
		inputRequest.setResponseClassName( sResponseClassName );
		inputRequest.setSenderType( sSenderClassName );
		return( inputRequest );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setID(org.homedns.mkh.dataservice.shared.Id)
	 */
	@Override
	public void setID( Id id ) {
		this.id = id;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getID()
	 */
	@Override
	public Id getID( ) {
		return( id );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getHandlerClassName()
	 */
	@Override
	public String getHandlerClassName( ) {
		return( sHandlerClassName );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setHandlerClassName(java.lang.String)
	 */
	@Override
	public void setHandlerClassName( String sHandlerClassName ) {
		this.sHandlerClassName = sHandlerClassName;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#isInitPresenter()
	 */
	@Override
	public boolean isInitPresenter( ) {
		return( bInitPresenter );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setInitPresenter(boolean)
	 */
	@Override
	public void setInitPresenter( boolean bInitPresenter ) {
		this.bInitPresenter = bInitPresenter;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getSenderType()
	 */
	@Override
	public String getSenderType( ) {
		return( sSenderClassName );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setSenderType(java.lang.String)
	 */
	@Override
	public void setSenderType( String sSenderClassName ) {
		this.sSenderClassName = sSenderClassName;
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#getResponseClassName()
	 */
	@Override
	public String getResponseClassName( ) {
		return( sResponseClassName );
	}

	/**
	 * @see org.homedns.mkh.dataservice.shared.Request#setResponseClassName(java.lang.String)
	 */
	@Override
	public void setResponseClassName( String sResponseClassName ) {
		this.sResponseClassName = sResponseClassName;
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.view.View#setBatchUpdate(boolean)
	 */
	public void setBatchUpdate( boolean bBatch ) {
		this.bBatch = bBatch;
	}

	/**
	 * @see org.homedns.mkh.dataservice.client.view.View#isBatchUpdate()
	 */
	public boolean isBatchUpdate( ) {
		return( bBatch );
	}
}
