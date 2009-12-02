package mikera.persistent.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mikera.persistent.*;
import mikera.persistent.impl.*;
import mikera.util.emptyobjects.NullList;

/**
 * Implements a persistent list that is a subset of an existing tuple
 * utilising the same immutable backing array
 * 
 * @author Mike
 *
 * @param <T>
 */
public final class SubList<T> extends BasePersistentList<T>   {	

	private static final long serialVersionUID = 3559316900529560364L;

	@SuppressWarnings("unchecked")
	public static final SubList EMPTY_SUBLIST = new SubList(NullList.INSTANCE,0,0);

	private final PersistentList<T> data;
	private final int offset;
	private final int length;
	
	@SuppressWarnings("unchecked")
	public static <T> SubList<T> create(List<T> source, int fromIndex, int toIndex) {
		if ((fromIndex<0)||(toIndex>source.size())) throw new IndexOutOfBoundsException();
		int newSize=toIndex-fromIndex;
		if (newSize<=0) {
			if (newSize==0) return SubList.EMPTY_SUBLIST;
			throw new IllegalArgumentException();
		}
		return create(ListFactory.createFromList(source),fromIndex,toIndex);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> SubList<T> create(PersistentList<T> source, int fromIndex, int toIndex) {
		if ((fromIndex<0)||(toIndex>source.size())) throw new IndexOutOfBoundsException();
		int newSize=toIndex-fromIndex;
		if (newSize<=0) {
			if (newSize==0) return SubList.EMPTY_SUBLIST;
			throw new IllegalArgumentException();
		}
		return createLocal(source,fromIndex,toIndex);
	}
	
	private static <T> SubList<T> createLocal(PersistentList<T> source, int fromIndex, int toIndex) {
		return new SubList<T>(source,fromIndex,toIndex-fromIndex);
	}
	
	public int size() {
		return length;
	}
	
	private SubList(PersistentList<T> source, int off, int len) {
		data=source;
		offset=off;
		length=len;	
	}
	
	public T get(int i) {
		if ((i<0)||(i>=length)) throw new IndexOutOfBoundsException();
		return data.get(i+offset);
	}
	
	public SubList<T> clone() {
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public PersistentList<T> subList(int fromIndex, int toIndex) {
		if ((fromIndex<0)||(toIndex>size())) throw new IndexOutOfBoundsException();
		if (fromIndex>=toIndex) return Tuple.EMPTY;
		if ((fromIndex==0)&&(toIndex==size())) return this;
		return data.subList(offset+fromIndex, toIndex-fromIndex);
	}
}
