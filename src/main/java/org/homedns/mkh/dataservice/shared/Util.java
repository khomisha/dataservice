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

package org.homedns.mkh.dataservice.shared;

import java.util.Random;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;

/**
 * Util methods
 */
public class Util {
	/**
	 * "standard" output stream
	 */
	public static final int CONSOLE 			= 0;
	/**
	 * alert message box
	 */
	public static final int MSG_BOX 			= 1;
	/**
	 * GWT hosted mode log
	 */
	public static final int HOST_MODE_LOG 		= 2;

	private static long lUID = System.currentTimeMillis( );
	private static final String CHARS = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,!,?,_,@,#,$,%,&,*,(,),-,+,=,1,2,3,4,5,6,7,8,9,0";
	
	/**
	 * Builds a string of the specified length by repeating the specified
	 * characters until the result string is long enough.
	 * 
	 * @param s
	 *            a string whose value will be repeated to fill the return
	 *            string
	 * @param iN
	 *            an integer whose value is the length of the string you want
	 *            returned
	 * 
	 * @return a string iN characters long filled with the characters in the
	 *         argument s. If the argument chars has more than iN characters,
	 *         the first iN characters of s are used to fill the return string.
	 *         If the argument s has fewer than iN characters, the characters in
	 *         s are repeated until the return string has iN characters. If any
	 *         argument's value is null, it returns null.
	 */
	public static String fill( String s, int iN ) {
		StringBuffer sb = new StringBuffer( );
		if( s.length( ) > iN ) {
			sb.append( s.substring( 0, iN ) );
		} else {
			int iCount = iN / s.length( );
			for( int i = 1; i <= iCount; i++ ) {
				sb.append( s );
			}
			if( iN - sb.length( ) > 0 ) {
				sb.append( s.substring( 0, iN - sb.length( ) ) );
			}
		}
		return( sb.toString( ) );
	}

	/**
	 * Fixes ordinary and double quotes in input string
	 * 
	 * @param s
	 *            the input string
	 * 
	 * @return result string.
	 */
	public static String fixQuotes( String s ) {
		if( s == null || s.equals( "" ) ) {
			return( s );
		}
		s = s.replaceAll( "'", "~'" );
		return( s.replaceAll( "\"", "~\"" ) );
	}

	/**
	 * Outputs error message
	 * 
	 * @param caught
	 *            throwable object
	 * @param iOutput
	 *            output type: {@link org.homedns.mkh.dataservice.shared.Util#CONSOLE}, {@link org.homedns.mkh.dataservice.shared.Util#MSG_BOX}, {@link org.homedns.mkh.dataservice.shared.Util#HOST_MODE_LOG} 
	 * @param sMsg
	 *            additional error information
	 */
	public static void signalMsg( Throwable caught, int iOutput, String sMsg ) {
		String sCauseMsg = getCauseMsg( caught );
		sMsg = ( 
			sMsg == null ? 
			"".equals( sCauseMsg ) ? "no detail message" : sCauseMsg : 
			sMsg + ". " + sCauseMsg
		);
		switch( iOutput ) {
			case CONSOLE:
				System.out.println( sMsg );
				break;
			case MSG_BOX:
				Window.alert( sMsg );
				break;
			case HOST_MODE_LOG:
				GWT.log( sMsg, caught );
				break;
			default:
				break;
		}
	}

	/**
	 * Sends debug message to the console.
	 * 
	 * @param sMsg
	 *            the debug message
	 */
	public static void printDebug( String sMsg ) {
		signalMsg( null, CONSOLE, sMsg );
	}

	/**
	 * Prints character hexadecimal representation.
	 * 
	 * @param c
	 *            the character to print as hexadecimal string
	 */
	public static void printChar( char c ) {
		printDebug( Integer.toHexString( c ) );
	}
	
	/**
	* Returns generated unique id.
	*/
	public static long getUID( ) {
		return( lUID++ );
	}

	/**
	 * Unwraps umbrella exception
	 * {@link com.google.web.bindery.event.shared.UmbrellaException}, returns
	 * unwrap exception
	 * 
	 * @param e
	 *            the input exception
	 * 
	 * @return the unwrap exception
	 */
	public static Throwable unwrap( Throwable e ) {   
		if( e instanceof UmbrellaException ) {   
			UmbrellaException ue = ( UmbrellaException ) e;  
			if( ue.getCauses( ).size( ) > 1 ) {   
				return unwrap( ue.getCauses( ).iterator( ).next( ) );  
			}  
		}  
		return( e );  
	}

	/**
	 * Returns cause error message
	 * 
	 * @param e
	 *            the input throwable
	 * 
	 * @return the cause error message
	 */
	public static String getCauseMsg( Throwable e ) {
		String sMsg = "";
		if( e != null ) {
			Throwable cause = e.getCause( );
			if( cause == null ) {
				sMsg = e.getMessage( ) == null ? e.toString( ) : e.getMessage( );
			} else {
				sMsg = getCauseMsg( cause );
			}
		}
		return( sMsg );
	}
	
	/**
	 * Returns random string of the specified length
	 * 
	 * @param iLength the length
	 *  
	 * @return random string
	 */
	public static String getRandomString( int iLength ) {
		String[] as = CHARS.split( "," );
		if( iLength <= 0 ) {
			return( null );
		}
		StringBuffer sb = new StringBuffer( );
		Random random = new Random( );
		for( int iIndex = 0; iIndex < iLength; iIndex++ ) {
			sb.append( as[ random.nextInt( as.length ) ] );
		}
		return( sb.toString( ) );
	}
}

