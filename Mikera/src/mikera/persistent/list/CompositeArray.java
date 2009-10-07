package mikera.persistent.list;

import java.util.List;

import mikera.persistent.*;

public class CompositeArray<T> extends BasePersistentArray<T> {
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
			if (b instanceof CompositeArray<?>) {
				CompositeArray<T> cb=(CompositeArray<T>)b;
				return new CompositeArray(concat(a,cb.front()),cb.back());
			}
		}
		
		if (b.size()<(a.size()>>1)) {
			if (a instanceof CompositeArray<?>) {
				CompositeArray<T> ca=(CompositeArray<T>)a;
				return new CompositeArray(ca.front(),concat(ca.back(),b));
			}
		}
		
		// TODO: balance!!
		return new CompositeArray(a,b);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> create(T[] data,  int fromIndex, int toIndex) {
		int n=toIndex-fromIndex;
		if (n<=ListFactory.MAX_TUPLE_BUILD_SIZE) return ListFactory.create(data, fromIndex, toIndex);
		int midIndex=n/ListFactory.MAX_TUPLE_BUILD_SIZE;
		return new CompositeArray(create(data,fromIndex,midIndex),create(data,midIndex,toIndex));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> create(List<T> source, int fromIndex, int toIndex) {
		if ((toIndex-fromIndex)<=ListFactory.MAX_TUPLE_BUILD_SIZE) {
			return Tuple.create(source, fromIndex, toIndex);
		}
		int midIndex=(fromIndex+toIndex)>>1;
		return new CompositeArray(create(source,fromIndex,midIndex),create(source,midIndex,toIndex));
	}
	
	private CompositeArray(PersistentList<T> a, PersistentList<T> b ) {
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
