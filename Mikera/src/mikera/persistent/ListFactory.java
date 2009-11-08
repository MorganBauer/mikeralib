package mikera.persistent;

import mikera.persistent.impl.CompositeList;
import mikera.persistent.impl.Singleton;
import mikera.persistent.impl.Tuple;
import mikera.util.emptyobjects.NullList;
import java.util.*;

public class ListFactory<T> {
	public static final int MAX_TUPLE_BUILD_SIZE=32;
	
	public static <T> PersistentList<T> create(T[] data) {
		return create(data,0,data.length);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> create(T[] data,  int fromIndex, int toIndex) {
		int n=toIndex-fromIndex;
		if (n==1) return Singleton.create(data[fromIndex]);
		if (n<=MAX_TUPLE_BUILD_SIZE) {
			// note this covers negative length case
			return (PersistentList<T>) Tuple.create(data,fromIndex,toIndex);
		}	
		return CompositeList.create(data,fromIndex,toIndex);
	}

	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> create(Collection<T> source) {
		if (source instanceof PersistentList<?>) {
			return (PersistentList<T>)source;
		} else if (source instanceof List<?>) {
			return create((List<T>)source,0,source.size());
		} 
		
		Object[] data=source.toArray();
		return create((T[])data);
	}
	
	public static<T> PersistentList<T> create(Iterator<T> source) {
		ArrayList<T> al=new ArrayList<T>();
		while(source.hasNext()) {
			al.add(source.next());
		}
		return create(al);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> create(List<T> source, int fromIndex, int toIndex) {
		int maxSize=source.size();
		if ((fromIndex<0)||(toIndex>maxSize)) throw new IndexOutOfBoundsException();
		if (fromIndex>=toIndex) {
			if (fromIndex==toIndex) return (PersistentList<T>)NullList.INSTANCE;
			throw new IllegalArgumentException();
		}
			
		int n=toIndex-fromIndex;
		
		// use sublist if possible
		if (source instanceof PersistentList) {
			if (n==maxSize) return (PersistentList)source;
			return (PersistentList<T>) ((PersistentList<T>)source).subList(fromIndex, toIndex);
		}
		
		if (n==1) return Singleton.create(source.get(fromIndex));
		if (n<=MAX_TUPLE_BUILD_SIZE) {
			// note this covers negative length case
			return Tuple.create(source,fromIndex,toIndex);
		}
		

		
		return CompositeList.create(source, fromIndex, toIndex);
	}
	
	public static <T> PersistentList<T> concat(PersistentList<T> a, PersistentList<T> b) {
		int as=a.size();
		if (as==0) return b;
		int bs=b.size();
		if (bs==0) return a;
		if ((as+bs)<=MAX_TUPLE_BUILD_SIZE) {
			return Tuple.concat(a,b);
		}
		
		return CompositeList.concat(a, b);
	}
}
