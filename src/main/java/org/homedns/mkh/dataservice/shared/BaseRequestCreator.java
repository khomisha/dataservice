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

import org.homedns.mkh.dataservice.shared.RequestFactory.RequestCreator;

/**
 * Default request creator
 *
 */
public class BaseRequestCreator implements RequestCreator {

	/**
	 * @see org.homedns.mkh.dataservice.client.Creator#instantiate(java.lang.Class)
	 */
	@Override
	public Request instantiate( Class< ? > type ) {
		Request request = null;
		if( type == ChangePasswordRequest.class ) {
			request = new ChangePasswordRequest( );
		} else if( type == ClosePagingConnRequest.class ) {
			request = new ClosePagingConnRequest( );
		} else if( type == DeleteRequest.class ) {
			request = new DeleteRequest( );
		} else if( type == GetDataBufferDescRequest.class ) {
			request = new GetDataBufferDescRequest( );
		} else if( type == InsertRequest.class ) {
			request = new InsertRequest( );
		} else if( type == LoadPageRequest.class ) {
			request = new LoadPageRequest( );
		} else if( type == LoginRequest.class ) {
			request = new LoginRequest( );
		} else if( type == LogoutRequest.class ) {
			request = new LogoutRequest( );
		} else if( type == RemoveDataBufferRequest.class ) {
			request = new RemoveDataBufferRequest( );
		} else if( type == ReportRequest.class ) {
			request = new ReportRequest( );
		} else if( type == RetrieveRequest.class ) {
			request = new RetrieveRequest( );
		} else if( type == UpdateRequest.class ) {
			request = new UpdateRequest( );
		}else if( type == StoredProcRequest.class ) {
			request = new StoredProcRequest( );
		}
		return( request );
	}
}
