package mikera.persistent;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mikera.persistent.*;
import mikera.persistent.list.*;
import mikera.util.emptyobjects.NullList;


public final class SubTuple<T> extends BasePersistentArray<T> implements PersistentList<T>  {	
	private final T[] data;
	private final int offset;
	private final int length;
	
	@SuppressWarnings("unchecked")
	static <T> SubTuple<T> create(T[] valuesDirect, int off, int len) {
		return new SubTuple(valuesDirect,off,len);
	}
	
	public int size() {
		return length;
	}
	
	private SubTuple(T[] valuesDirect, int off, int len) {
		data=valuesDirect;
		offset=off;
		length=len;	
	}
	
	public T get(int i) {
		if ((i<0)||(i>=length)) throw new IndexOutOfBoundsException();
		return data[i+offset];
	}
	
	public SubTuple<T> clone() {
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public PersistentList<T> subList(int fromIndex, int toIndex) {
		if ((fromIndex<0)||(toIndex>size())) throw new IndexOutOfBoundsException();
		if (fromIndex>=toIndex) return Tuple.EMPTY;
		if ((fromIndex==0)&&(toIndex==size())) return this;
		return SubTuple.create(data, offset+fromIndex, toIndex-fromIndex);
	}

}
