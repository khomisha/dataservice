/*
 * Copyright 2007-2014 Mikhail Khodonov
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

import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The interface for the RPC server end point to get data for clients that
 * will be calling asynchronous.
 */
public interface DataServiceAsync {
	/**
	 * Remote procedure call
	 * 
	 * @param request
	 *            the request to do
	 * @param callback
	 *            the callback object
	 */
	public void doRPC( Request request, AsyncCallback< Response > callback );
}
