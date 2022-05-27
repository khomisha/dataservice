/*
 * Copyright 2013-2022 Mikhail Khodonov
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Data object to transfer data from client to server and vice versa
 *
 */
public class Data implements List< List< Serializable > >, Serializable {
	private static final long serialVersionUID = -6272834019384174268L;
	
	private List< List< Serializable > > list;

	public Data( ) { 
		list = new ArrayList< List< Serializable > >( );
	}

	/**
	 * Adds row
	 * 
	 * @return the row index to add
	 */
	public int addRow( ) {
		List< Serializable > row = new ArrayList< Serializable >( );
		add( row );
		return( size( ) - 1 );
	}
	
	/**
	 * Appends value to the last row. If data object is empty, it adds row first
	 * 
	 * @param value
	 *            the value to add
	 */
	public < T extends Serializable > void addValue( T value ) {
		if( isEmpty( ) ) {
			addRow( );
		}
		List< Serializable > row = get( size( ) - 1 );
		row.add( value );
	}
	
	/**
	 * Returns specified cell value
	 * 
	 * @param iRow
	 *            the cell row index
	 * @param iCol
	 *            the cell column index
	 * @return the data cell value
	 */
	public Object getValue( int iRow, int iCol ) {
		return( get( iRow ).get( iCol ) );
	}

	/**
	 * Returns data row
	 * 
	 * @param iRow
	 *            the row index
	 * 
	 * @return the data row
	 */
	public List< Serializable > getRow( int iRow ) {
		return( get( iRow ) );
	}
	
	/**
	 * Removes row
	 * 
	 * @param iRow
	 *            the row index to remove
	 */
	public void removeRow( int iRow ) {
		remove( iRow );
	}

	/**
	 * Removes value from specified row, column
	 * 
	 * @param iRow
	 *            the row index
	 * @param iCol
	 *            the column index
	 */
	public void removeValue( int iRow, int iCol ) {
		get( iRow ).remove( iCol );
	}
	
	/**
	 * Sets value to the specified cell
	 * 
	 * @param iRow
	 *            the cell row index where value to set
	 * @param iCol
	 *            the cell column index where value to set
	 * @param value
	 *            the value to set
	 */
	public < T extends Serializable > void setValue( int iRow, int iCol, T value ) {
		get( iRow ).set( iCol, value );
	}

	/**
	 * @see java.lang.Iterable#forEach(java.util.function.Consumer)
	 */
	public void forEach( Consumer< ? super List< Serializable > > action ) {
		list.forEach( action );
	}

	/**
	 * @see java.util.List#size()
	 */
	public int size( ) {
		return list.size( );
	}

	/**
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty( ) {
		return list.isEmpty( );
	}

	/**
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains( Object o ) {
		return list.contains( o );
	}

	/**
	 * @see java.util.List#iterator()
	 */
	public Iterator< List< Serializable > > iterator( ) {
		return list.iterator( );
	}

	/**
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray( ) {
		return list.toArray( );
	}

	/**
	 * @see java.util.List#toArray(java.lang.Object[])
	 */
	public < T > T[] toArray( T[] a ) {
		return list.toArray( a );
	}

	/**
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add( List< Serializable > e ) {
		return list.add( e );
	}

	/**
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove( Object o ) {
		return list.remove( o );
	}

	/**
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll( Collection< ? > c ) {
		return list.containsAll( c );
	}

	/**
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll( Collection< ? extends List< Serializable > > c ) {
		return list.addAll( c );
	}

	/**
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll( int index, Collection< ? extends List< Serializable > > c ) {
		return list.addAll( index, c );
	}

	/**
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll( Collection< ? > c ) {
		return list.removeAll( c );
	}

	/**
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll( Collection< ? > c ) {
		return list.retainAll( c );
	}

	/**
	 * @see java.util.List#replaceAll(java.util.function.UnaryOperator)
	 */
	public void replaceAll( UnaryOperator< List< Serializable > > operator ) {
		list.replaceAll( operator );
	}

	/**
	 * @see java.util.Collection#removeIf(java.util.function.Predicate)
	 */
	public boolean removeIf( Predicate< ? super List< Serializable > > filter ) {
		return list.removeIf( filter );
	}

	/**
	 * @see java.util.List#sort(java.util.Comparator)
	 */
	public void sort( Comparator< ? super List< Serializable > > c ) {
		list.sort( c );
	}

	/**
	 * @see java.util.List#clear()
	 */
	public void clear( ) {
		list.clear( );
	}

	/**
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals( Object o ) {
		return list.equals( o );
	}

	/**
	 * @see java.util.List#hashCode()
	 */
	public int hashCode( ) {
		return list.hashCode( );
	}

	/**
	 * @see java.util.List#get(int)
	 */
	public List< Serializable > get( int index ) {
		return list.get( index );
	}

	/**
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public List< Serializable > set( int index, List< Serializable > element ) {
		return list.set( index, element );
	}

	/**
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add( int index, List< Serializable > element ) {
		list.add( index, element );
	}

	/**
	 * @see java.util.Collection#stream()
	 */
	public Stream< List< Serializable > > stream( ) {
		return list.stream( );
	}

	/**
	 * @see java.util.List#remove(int)
	 */
	public List< Serializable > remove( int index ) {
		return list.remove( index );
	}

	/**
	 * @see java.util.Collection#parallelStream()
	 */
	public Stream< List< Serializable > > parallelStream( ) {
		return list.parallelStream( );
	}

	/**
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf( Object o ) {
		return list.indexOf( o );
	}

	/**
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf( Object o ) {
		return list.lastIndexOf( o );
	}

	/**
	 * @see java.util.List#listIterator()
	 */
	public ListIterator< List< Serializable > > listIterator( ) {
		return list.listIterator( );
	}

	/**
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator< List< Serializable > > listIterator( int index ) {
		return list.listIterator( index );
	}

	/**
	 * @see java.util.List#subList(int, int)
	 */
	public List< List< Serializable > > subList( int fromIndex, int toIndex ) {
		return list.subList( fromIndex, toIndex );
	}

	/**
	 * @see java.util.List#spliterator()
	 */
	public Spliterator< List< Serializable > > spliterator( ) {
		return list.spliterator( );
	}
}