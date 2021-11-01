/*
 * Copyright 2014-2021 Mikhail Khodonov
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

package org.homedns.mkh.dataservice.server;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import com.google.gwt.core.server.StackTraceDeobfuscator;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Remote logging service implementation. 
 * Usage:
 * <pre>
 * 1. switch on logging and define logging parameters in your-gwt-module-name.gwt.xml (remote logging, log level etc.): 
 *     &#060;inherits name="com.google.gwt.logging.Logging" /&#062;
 *     &#060;set-property name="gwt.logging.simpleRemoteHandler" value="ENABLED" /&#062; # To enable a remote Handler
 * 	
 *     &#060;set-property name="gwt.logging.logLevel" value="SEVERE"/&#062;          # To change the default logLevel
 *     &#060;set-property name="gwt.logging.enabled" value="TRUE"/&#062;             # To enable logging
 *     &#060;set-property name="gwt.logging.consoleHandler" value="DISABLED"/&#062;  # To disable a default Handler
 * 2. define remote logging servlet class and symbol maps location (for deobfuscate) in web.xml 
 *     &#060;servlet&#062;  
 *         &#060;servlet-name>remoteLogging&#060;/servlet-name&#062;  
 *         &#060;servlet-class>your-remote-logging-servlet-class&#060;/servlet-class&#062; # e.g. org.homedns.mkh.nftestm.server.RemoteLoggingServiceImpl  
 *     &#060;/servlet&#062; 
 * 
 *     &#060;init-param>
 *         &#060;param-name>symbolMaps&#060;/param-name&#062;
 *         &#060;param-value>WEB-INF/deploy/your-gwt-module-name/symbolMaps/&#060;/param-value&#062;
 *     &#060;/init-param&#062;
 * 
 *     &#060;servlet-mapping&#062;  
 *         &#060;servlet-name&#062;remoteLogging&#060;/servlet-name&#062;  
 *         &#060;url-pattern&#062;/your-gwt-module-name/remote_logging&#060;/url-pattern&#062;  
 *     &#060;/servlet-mapping&#062; 
 * </pre>   
 */
@SuppressWarnings( "serial" )
public abstract class AbstractRemoteLoggingService extends RemoteServiceServlet implements RemoteLoggingService {
	private static final Logger LOG = Logger.getLogger( AbstractRemoteLoggingService.class );

	private StackTraceDeobfuscator deobfuscator;
	
	/**
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init( ) throws ServletException {
		super.init( );
		try {
			String sPath = getServletContext( ).getResource( "/" ).getPath( ) + getSymbolMapPath( );
			setDeobfuscator( StackTraceDeobfuscator.fromResource( sPath ) );
			LOG.debug( "symbol maps path: " + sPath );
		}
		catch( Exception e ) {
			ServletException se = new ServletException( );
			se.initCause( e );
			throw se;
		}
	}

	/**
	 * @see com.google.gwt.logging.shared.RemoteLoggingService#logOnServer(java.util.logging.LogRecord)
	 */
	@Override
	public String logOnServer( LogRecord record ) {
		try { 
			Throwable t = record.getThrown( );
			if( t != null ) {
				StackTraceElement[] sts = deobfuscator.resymbolize( t.getStackTrace( ), getPermutationStrongName( ) );
				t.setStackTrace( sts );  
				deobfuscator.deobfuscateStackTrace( t, getPermutationStrongName( ) );
			}
			logData( 
				record.getLevel( ), 
				getThreadLocalRequest( ).getRemoteAddr( ) + ": " + record.getMessage( ), 
				t 
			);
		}
		catch( Exception e ) {
			String sErr = Context.getLocalizedMsg( "loggingFailed" );
			LOG.fatal( sErr, e );
			return( sErr );
		}
		return( null );
	}
	
	/**
	 * Returns deobfuscator
	 * 
	 * @return the deobfuscator
	 */
	public StackTraceDeobfuscator getDeobfuscator( ) {
		return( deobfuscator );
	}

	/**
	 * Sets deobfuscator
	 * 
	 * @param deobfuscator
	 *            the deobfuscator to set
	 */
	public void setDeobfuscator( StackTraceDeobfuscator deobfuscator ) {
		this.deobfuscator = deobfuscator;
	}

	/**
	 * Returns symbol map path
	 * 
	 * @return the symbol map path
	 */
	abstract protected String getSymbolMapPath( );
	
	/**
	 * Logs data
	 * 
	 * @param level
	 *            the logging level
	 * @param sMessage
	 *            the message to log
	 * @param t
	 *            the throwable to log
	 */
	private void logData( Level level, String sMessage, Throwable t ) {
		if( level == Level.SEVERE ) {
			LOG.error( sMessage, t );
		} else if( level == Level.WARNING ) {
			LOG.warn( sMessage, t );
		} else if( level == Level.INFO ) {
			LOG.info( sMessage, t );
		} else if( level == Level.CONFIG ) {
			LOG.debug( sMessage, t );
		} else if( level == Level.FINE || level == Level.FINER || level == Level.FINEST ) {
			LOG.trace( sMessage, t );
		} 
	}
}
