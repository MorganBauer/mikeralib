package mikera.persistent.impl;

import mikera.persistent.PersistentList;
import mikera.persistent.PersistentSet;
import mikera.util.emptyobjects.NullList;

/**
 * Singleton list instance
 * 
 * @author Mike Anderson
 *
 * @param <T>
 */
public final class SingletonList<T> extends BasePersistentList<T> {

	private static final long serialVersionUID = 8273587747838774580L;
	
	final T value;
	
	@SuppressWarnings("unchecked")
	public static <T> SingletonList create(T object) {
		return new SingletonList<T>(object);
	}
	
	public int size() {
		return 1;
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	public T get(int i) {
		if (i==0) return value;
		throw new IndexOutOfBoundsException();
	}
	
	private SingletonList(T object) {
		value=object;
	}
	
	public PersistentList<T> front() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public PersistentList<T> back() {
		return (PersistentList<T>) NullList.INSTANCE;
	}
}
