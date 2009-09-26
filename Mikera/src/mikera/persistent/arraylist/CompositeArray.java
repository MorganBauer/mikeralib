package mikera.persistent.arraylist;

import mikera.persistent.*;

public class CompositeArray<T> implements PersistentArray<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final PersistentArray<T> front;
	public final PersistentArray<T> back;
	public final int size;
	
	public static <T> CompositeArray<T> concat(PersistentArray<T> a, PersistentArray<T> b) {
		throw new UnsupportedOperationException();
	}
	
	private CompositeArray(PersistentArray<T> a, PersistentArray<T> b ) {
		front =a;
		back=b;
		size=a.size()+b.size();
	}
	
	public int hashCode() {
		return hashCode();
	}

	public T get(int i) {
		int fs=front.size();
		if (i<fs) {
			return front.get(i);
		} else {
			return back.get(i-fs);
		}
	}

	public int size() {
		return size;
	}


	public PersistentArray<T> append(T value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PersistentArray<T> append(PersistentArray<T> value) {
		// TODO Auto-generated method stub
		return null;
	}

	public int end() {
		return back.end();
	}

	public int start() {
		return front.start();
	}
}
