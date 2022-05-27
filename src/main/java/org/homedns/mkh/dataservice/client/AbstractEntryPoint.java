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

package org.homedns.mkh.dataservice.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.homedns.mkh.dataservice.shared.Util;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @see com.google.gwt.core.client.EntryPoint
 *
 */
public abstract class AbstractEntryPoint implements EntryPoint {
	private static final Logger LOG = Logger.getLogger( AbstractEntryPoint.class.getName( ) );  

	private static DataServiceAsync _ds;
	private static Constants _constants;
	private static Messages _messages;
	private static String _sDownloadURL;

	/**
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	@Override
	public void onModuleLoad( ) {
		GWT.setUncaughtExceptionHandler(
			new GWT.UncaughtExceptionHandler( ) {  
				public void onUncaughtException( Throwable e ) {  
					e.printStackTrace( );
					LOG.log( Level.SEVERE, "UncaughtException", Util.unwrap( e ) );
			    }
			}
		);
			
		// use deferred command to catch initialization exceptions in onStartApplication( )
	    Scheduler.get( ).scheduleDeferred( 
	    	new ScheduledCommand( ) {
	    		@Override
	    		public void execute( ) {
	    			onStartApplication( );
	    		}
	    	}
	    );
	}

	/**
	 * Starts application
	 */
	protected void onStartApplication( ) {
		// Create the client proxy.
		_ds = ( DataServiceAsync )GWT.create( DataService.class );

		// Specify the URL at which our service implementation is running.
		// Note that the target URL must reside on the same domain and port from
		// which the host page was served.
		setServiceURL( ( ServiceDefTarget )_ds, "dataservice" );
	}
	
	/**
	* Returns data service.
	*
	* @return data service
	*/
	public static DataServiceAsync getDataService( ) {
		return( _ds );
	}

	/**
	 * Returns i18n constants object
	 * 
	 * @return the i18n constants object
	 */
	public static Constants getConstants( ) {
		return( _constants );
	}

	/**
	 * Returns i18n messages object
	 * 
	 * @return the i18n messages object
	 */
	public static Messages getMessages( ) {
		return( _messages );
	}

	/**
	 * Sets i18n constants object
	 * 
	 * @param constants the i18n constants object to set
	 */
	public static void setConstants( Constants constants ) {
		_constants = constants;
	}

	/**
	 * Sets i18n messages object
	 * 
	 * @param messages the i18n messages object to set
	 */
	public static void setMessages( Messages messages ) {
		_messages = messages;
	}
	
	/**
	* Returns download servlet url
	* 
	* @return download servlet url
	*/
	public static String getDownloadURL( ) {
		return( _sDownloadURL );
	}

	/**
	* Sets download servlet url
	* 
	* @param sDownloadURL servlet url
	*/
	public static void setDownloadURL( String sDownloadURL ) {
		_sDownloadURL = sDownloadURL;
	}

	/**
	 * Sets service URL
	 * 
	 * @param endPoint
	 *            the service definition target
	 * @param sServiceDomain
	 *            the service domain
	 */
	private void setServiceURL( ServiceDefTarget endPoint, String sServiceDomain ) {
		endPoint.setServiceEntryPoint( GWT.getModuleBaseURL( ) + sServiceDomain );		
		LOG.config( endPoint.getServiceEntryPoint( ) );  
	}
}
