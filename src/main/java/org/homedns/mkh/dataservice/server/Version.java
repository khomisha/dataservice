/*
* Copyright 2012-2014 Mikhail Khodonov
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
 * $Id: Version.java 5 2013-09-09 07:05:36Z khomisha $
*/

package org.homedns.mkh.dataservice.server;

/**
 * Package version stamp
 *
 */
public class Version {

	private Version( ) {
		//this prevents even the native class from
		//calling this constructor as well :
		throw new AssertionError( );
	}

	/**
	 * Returns version
	 * 
	 * @return the version
	 */
	public static String getVersion( ) {
		return( Version.class.getPackage( ).getImplementationVersion( ) );
	}
}
