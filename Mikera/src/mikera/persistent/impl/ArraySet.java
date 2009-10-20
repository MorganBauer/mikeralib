package mikera.persistent.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mikera.persistent.Text;

/**
 * Array based set implementation
 * 
 * @author Mike
 *
 * @param <T>
 */
public final class ArraySet<T> extends BasePersistentSet<T> {
	private final T[] data;
	
	@SuppressWarnings("unchecked")
	public static <T> ArraySet<T> create(Set<T> source) {
		return new ArraySet<T>((T[])source.toArray());
	}
	
	public static <T> ArraySet<T> create(T[] source) {
		HashSet<T> hs=new HashSet<T>();
		for (int i=0; i<source.length; i++) {
			hs.add(source[i]);
		}
		return create(hs);
	}
	
	private ArraySet(T[] newData) {
		data=newData;
	}
	
	public Iterator<T> iterator() {
		return new ArraySetIterator<T>();
	}

	public int size() {
		return data.length;
	}
	
	private class ArraySetIterator<K> implements Iterator<K> {
		private int pos=0;
		
		public boolean hasNext() {
			return pos<data.length;
		}

		@SuppressWarnings("unchecked")
		public K next() {
			return (K)data[pos++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	private static final long serialVersionUID = -3125683703717134995L;

}
