package mikera.persistent.list;

import mikera.persistent.*;

public class CompositeArray<T> extends BasePersistentArray<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final PersistentList<T> front;
	public final PersistentList<T> back;
	private final int size;
	
	public static <T> CompositeArray<T> concat(PersistentList<T> a, PersistentList<T> b) {
		throw new UnsupportedOperationException();
	}
	
	private CompositeArray(PersistentList<T> a, PersistentList<T> b ) {
		front=a;
		back=b;
		size=a.size()+b.size();
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


	public PersistentList<T> append(T value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PersistentList<T> append(PersistentList<T> value) {
		// TODO Auto-generated method stub
		return null;
	}

	public int hashCode() {
		int r= Integer.rotateRight(front.hashCode(),back.size());
		r^=back.hashCode();
		return r;
	}
}
