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

package org.homedns.mkh.dataservice.client.presenter;

import org.homedns.mkh.dataservice.client.Creator;
import org.homedns.mkh.dataservice.shared.Id;
import com.google.gwt.core.shared.GWT;

/**
 * Presenter factory
 *
 */
public class PresenterFactory {	
	
	/**
	 * Presenter creator
	 */
	public interface PresenterCreator extends Creator< Presenter > {
	}
	
	private static PresenterCreator _creator;

	/**
	 * Creates new presenter.
	 * 
	 * @param type
	 *            the presenter type
	 * @param id
	 *            the presenter identification object
	 * 
	 * @return the presenter
	 */
	public static Presenter create( Class< ? > type, Id id ) {
		if( _creator == null ) {
			_creator = GWT.create( PresenterCreator.class );
		}
		Presenter p = _creator.instantiate( type );
		if( id != null ) {
			p.setID( id );
		}
		return( p );
	}
}
