/*
 * Copyright 2016 Mikhail Khodonov
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

/**
 * Name value pair
 *
 */
public class Pair< T, V > {
	private T name;
	private V value;

	/**
	 * @param name the name
	 * @param value the value
	 */
	public Pair( T name, V value ) {
		setName( name );
		setValue( value );
	}

	/**
	 * @return the name
	 */
	public T getName( ) {
		return name;
	}

	/**
	 * @return the value
	 */
	public V getValue( ) {
		return value;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( T name ) {
		this.name = name;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue( V value ) {
		this.value = value;
	}
}
