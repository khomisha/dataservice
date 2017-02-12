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
 * $Id: ViewWidget.java 6 2013-09-09 07:17:19Z khomisha $
 */

package org.homedns.mkh.dataservice.client.view;

import org.homedns.mkh.dataservice.shared.Data;
import org.homedns.mkh.dataservice.shared.Request;
import org.homedns.mkh.dataservice.shared.Response;
import org.homedns.mkh.dataservice.shared.Id;
import com.google.gwt.user.client.Command;

/**
 * View (UI components) interface
 *
 */
public interface View {
	
	/**
	 * Sets batch update flag
	 * 
	 * @param bBatch the batch update flag to set
	 */
	public void setBatchUpdate( boolean bBatch );
	
	/**
	 * Returns true if batch update and false if otherwise
	 * 
	 * @return batch update flag
	 */
	public boolean isBatchUpdate( );
		
	/**
	 * Returns view data cache
	 * 
	 * @return the view data cache
	 */
	public ViewCache getCache( );
	
	/**
	 * Returns view description object
	 * 
	 * @return the view description object
	 */
	public ViewDesc getDescription( );

	/**
	 * Returns view identification object
	 * 
	 * @return the view identification object
	 */
	public Id getID( );
	
	/**
	 * Sets data as string (json or xml) to the specified request
	 * 
	 * @param request
	 *            the request to set data
	 * 
	 */
	public void getSavingData( Request request );

	/**
	 * Inits view, typically it shouldn't be directly called. It is called by
	 * presenter
	 * 
	 * @param desc
	 *            the view description object
	 */
	public void init( ViewDesc desc );
	
	/**
	 * Returns view init flag
	 * 
	 * @return the view init flag
	 */
	public boolean isInit( );

	/**
	 * Invokes on request sending.
	 * 
	 * @param request
	 *            the request to send
	 */
	public void onSend( Request request );
	
	/**
	 * Invokes on view initialization, if necessary extends request definition.
	 * 
	 * @return the request object
	 */
	public Request onInit( );
	
	/**
	 * Invokes view action on server response
	 * 
	 * @param data
	 *            the data from server
	 */
	public void onResponse( Response data );

	/**
	 * Sets view data cache
	 * 
	 * @param cache
	 *            the data cache to set
	 */
	public void setCache( ViewCache cache );
	
	/**
	 * Sets view identification object to uniquely identify bound data presenter.
	 * 
	 * @param id
	 *            the view identification object to set
	 */
	public void setID( Id id );
	
	/**
	 * Returns retrieval arguments
	 * 
	 * @return the retrieval arguments
	 */
	public Data getArgs( );

	/**
	 * Sets retrieval arguments
	 * 
	 * @param args
	 *            the retrieval arguments to set
	 */
	public void setArgs( Data args );
	
	/**
	 * Returns cache type
	 * 
	 * @return the cache type
	 */
	public Class< ? > getCacheType( );
	
	/**
	 * Sets cache type
	 * 
	 * @param type
	 *            the cache type to set
	 */
	public void setCacheType( Class< ? > type );
	
	/**
	 * Sets after view init callback command
	 * 
	 * @param cmd
	 *            the command to set
	 */
	public void setAfterInitCommand( Command cmd );
	
	/**
	 * Refreshes view
	 */
	public void refresh( );
	
	/**
	 * Returns server response 
	 * 
	 * @return the response
	 */
	public Response getResponse( );

	/**
	 * Sets server response
	 * 
	 * @param response the response to set
	 */
	public void setResponse( Response response );
	
	/**
	 * Reloads view
	 */
	public void reload( );
}