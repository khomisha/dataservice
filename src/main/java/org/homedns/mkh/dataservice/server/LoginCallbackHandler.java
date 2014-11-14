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

package org.homedns.mkh.dataservice.server;

import java.io.IOException;
import java.util.Locale;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Login callback handler
 *
 */
public class LoginCallbackHandler implements CallbackHandler {
	private String _sLogin;
	private String _sPassword;
	private String _sLocale;
	private String _sClientDateTimeFmt;
			  
	/**
	 * @param sLogin
	 *            the user login
	 * @param sPassword
	 *            the user password
	 * @param sLocale
	 *            the locale
	 * @param sClientDateTimeFmt
	 *            the client date time format
	 */
	public LoginCallbackHandler( 
		String sLogin, String sPassword, String sLocale, String sClientDateTimeFmt 
	) {
		 _sLogin = sLogin;
		 _sPassword = sPassword;
		 _sLocale = sLocale;
		 _sClientDateTimeFmt = sClientDateTimeFmt;
	 }

	 /**
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */			 		 
	@Override
	public void handle( Callback[] callbacks ) throws IOException, UnsupportedCallbackException { 
	     for( Callback callback : callbacks ) {
	        if( callback instanceof NameCallback ) {
	           NameCallback nameCallback = ( NameCallback )callback;
	           nameCallback.setName( _sLogin );
	        } else if( callback instanceof PasswordCallback ) {
	           PasswordCallback passwordCallback = ( PasswordCallback )callback;
	           passwordCallback.setPassword( _sPassword.toCharArray( ) );
	        } else if( callback instanceof LanguageCallback ) {
	        	LanguageCallback langCallback = ( LanguageCallback )callback;
	        	langCallback.setLocale( new Locale( _sLocale ) );
	        } else if( callback instanceof TextInputCallback ) {
	        	TextInputCallback textCallback = ( TextInputCallback )callback;
	        	textCallback.setText( _sClientDateTimeFmt );
	        } else {
	           throw new UnsupportedCallbackException( callback );
	        }
	    }
	}
}
