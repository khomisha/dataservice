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

package org.homedns.mkh.dataservice.client.view;

import org.homedns.mkh.dataservice.client.Creator;
import org.homedns.mkh.dataservice.client.DataBufferDesc;
import com.google.gwt.core.shared.GWT;

/**
 * View description factory
 *
 */
public class ViewDescFactory {

	/**
	 * View description creator
	 */
	public interface ViewDescCreator extends Creator< ViewDesc > {
	}
	
	private static ViewDescCreator _creator;

	/**
	 * Creates view description
	 * 
	 * @param type
	 *            the view description class type
	 * @param desc
	 *            the data buffer description object
	 *            
	 * @return the view description
	 */
	public static ViewDesc create( Class< ? > type, DataBufferDesc desc ) {
		if( _creator == null ) {
			_creator = GWT.create( ViewDescCreator.class );
		}
		ViewDesc viewDesc = _creator.instantiate( type );
		viewDesc.setDataBufferDesc( desc );
		return( viewDesc );
	}
}
