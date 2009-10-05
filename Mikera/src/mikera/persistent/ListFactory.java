package mikera.persistent;

import mikera.persistent.list.CompositeArray;
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
		return CompositeArray.create(data,fromIndex,toIndex);
	}

	public static <T> PersistentList<T> create(List<T> source) {
		if (source instanceof PersistentList<?>) {
			return (PersistentList<T>)source;
		}
		return create(source,0,source.size());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PersistentList<T> create(List<T> source, int fromIndex, int toIndex) {
		if (fromIndex>=toIndex) {
			if (fromIndex==toIndex) return (PersistentList<T>)NullList.INSTANCE;
			throw new IllegalArgumentException();
		}
			
		int n=toIndex-fromIndex;
		if (n==1) return Singleton.create(source.get(fromIndex));
		if (n<=MAX_TUPLE_BUILD_SIZE) {
			// note this covers negative length case
			return Tuple.create(source,fromIndex,toIndex);
		}
		
		if (source instanceof PersistentList) {
			return ((PersistentList)source).subList(fromIndex, toIndex);
		}
		
		return CompositeArray.create(source, fromIndex, toIndex);
	}
	
	public static <T> PersistentList<T> concat(PersistentList<T> a, PersistentList<T> b) {
		int as=a.size();
		if (as==0) return b;
		int bs=b.size();
		if (bs==0) return a;
		if ((as+bs)<=MAX_TUPLE_BUILD_SIZE) {
			return Tuple.concat(a,b);
		}
		
		return CompositeArray.concat(a, b);
	}
}
