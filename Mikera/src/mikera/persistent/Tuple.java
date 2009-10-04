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


public final class Tuple<T> extends BasePersistentArray<T>  implements PersistentList<T>  {
	
	private final T[] data;
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> create(T[] values) {
		int n=values.length;
		T[] ndata=(T[]) new Object[n];
		System.arraycopy(values,0,ndata,0,n);
		return new Tuple(ndata);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> create(T a) {
		T[] ndata=(T[])new Object[1];
		ndata[0]=a;
		return new Tuple(ndata);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> create(T a, T b) {
		T[] ndata=(T[])new Object[2];
		ndata[0]=a;
		ndata[1]=b;
		return new Tuple(ndata);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> create(T[] values, int fromIndex, int toIndex) {
		int n=toIndex-fromIndex;
		T[] ndata=(T[]) new Object[n];
		for (int i=0; i<n; i++) {
			ndata[i]=values[i+fromIndex];
		}
		return new Tuple(ndata);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> create(List<T> values) {
		int n=values.size();
		T[] ndata=(T[]) new Object[n];
		for (int i=0; i<n; i++) {
			ndata[i]=values.get(i);
		}
		return new Tuple(ndata);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> create(List<T> values, int fromIndex, int toIndex) {
		int n=toIndex-fromIndex;
		T[] ndata=(T[]) new Object[n];
		for (int i=0; i<n; i++) {
			ndata[i]=values.get(i+fromIndex);
		}
		return new Tuple(ndata);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tuple<T> concat(List<T> a, List<T> b) {
		int as=a.size();
		int bs=b.size();
		T[] ndata=(T[]) new Object[as+bs];
		for (int i=0; i<as; i++) {
			ndata[i]=a.get(i);
		}
		for (int i=0; i<bs; i++) {
			ndata[as+i]=b.get(i);
		}

		return new Tuple(ndata);
	}
	
	public int size() {
		return data.length;
	}
	
	private Tuple(T[] values) {
		data=values;
	}
	
	public T get(int i) {
		return data[i];
	}
	
	public Tuple<T> clone() {
		return this;
	}

}
