package mikera.persistent.impl;

import java.util.List;

import mikera.persistent.*;

public class CompositeList<T> extends BasePersistentList<T> {
	private static final long serialVersionUID = 1L;
	
	public final PersistentList<T> front;
	public final PersistentList<T> back;
	private final int size;
	
	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> concat(PersistentList<T> a, PersistentList<T> b) {
		int as=a.size();
		int bs=b.size();
		if ((as+bs)<=ListFactory.MAX_TUPLE_BUILD_SIZE) {
			return Tuple.concat(a, b);
		}
		
		if (a.size()<(b.size()>>1)) {
			if (b instanceof CompositeList<?>) {
				CompositeList<T> cb=(CompositeList<T>)b;
				return new CompositeList(concat(a,cb.front()),cb.back());
			}
		}
		
		if (b.size()<(a.size()>>1)) {
			if (a instanceof CompositeList<?>) {
				CompositeList<T> ca=(CompositeList<T>)a;
				return new CompositeList(ca.front(),concat(ca.back(),b));
			}
		}
		
		// TODO: balance!!
		return new CompositeList(a,b);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> CompositeList<T> create(T[] data,  int fromIndex, int toIndex) {
		int n=toIndex-fromIndex;
		int midIndex=fromIndex+((n>>1)/ListFactory.MAX_TUPLE_BUILD_SIZE)*ListFactory.MAX_TUPLE_BUILD_SIZE;
		return new CompositeList(ListFactory.create(data,fromIndex,midIndex),ListFactory.create(data,midIndex,toIndex));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> CompositeList<T> create(List<T> source, int fromIndex, int toIndex) {
		int n=toIndex-fromIndex;
		int midIndex=fromIndex+((n>>1)/ListFactory.MAX_TUPLE_BUILD_SIZE)*ListFactory.MAX_TUPLE_BUILD_SIZE;
		return new CompositeList(ListFactory.create(source,fromIndex,midIndex),ListFactory.create(source,midIndex,toIndex));
	}
	
	private CompositeList(PersistentList<T> a, PersistentList<T> b ) {
		front=a;
		back=b;
		size=a.size()+b.size();
	}
	
	public PersistentList<T> subList(int fromIndex, int toIndex) {
		if ((fromIndex<0)||(toIndex>size)) throw new IndexOutOfBoundsException();
		if ((fromIndex==0)&&(toIndex==size)) return this;
		int fs=front.size();
		if (toIndex<=fs) return front.subList(fromIndex, toIndex);
		if (fromIndex>=fs) return back.subList(fromIndex-fs, toIndex-fs);
		return concat(front.subList(fromIndex, fs),back.subList(0, toIndex-fs));
	}
	
	public PersistentList<T> front() {
		return front;
	}

	public PersistentList<T> back() {
		return back;
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
		return concat(this,Tuple.create(value));
	}

	public PersistentList<T> append(PersistentList<T> value) {
		return concat(this,value);
	}

	public int hashCode() {
		int r= Integer.rotateRight(front.hashCode(),back.size());
		r^=back.hashCode();
		return r;
	}
}
