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

package org.homedns.mkh.dataservice.client.sender;

import org.homedns.mkh.dataservice.shared.Request;
import com.google.gwt.core.shared.GWT;

/**
 * Creates sender and binds it with appropriate request 
 *
 */
public class SendBinder {
	private static RequestSenderCreator _creator;

	/**
	 * Creates sender for specified request and binds it
	 * 
	 * @param request
	 *            the request for which sender is created
	 * 
	 * @return the request sender
	 */
	public static RequestSender bind( Request request ) {
		if( _creator == null ) {
			_creator = GWT.create( RequestSenderCreator.class );
		}
		RequestSender sender = _creator.instantiate( request.getSenderType( ) );
		sender.setRequest( request );
		return( sender );
	}
}
