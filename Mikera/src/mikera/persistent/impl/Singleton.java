package mikera.persistent.impl;

import mikera.persistent.PersistentList;
import mikera.persistent.PersistentSet;

/**
 * Singleton instance
 * 
 * Note: can be considered either as a list or a set
 * 
 * @author Mike Anderson
 *
 * @param <T>
 */
public final class Singleton<T> extends BasePersistentList<T> {

	private static final long serialVersionUID = 8273587747838774580L;
	
	final T value;
	
	@SuppressWarnings("unchecked")
	public static <T> Singleton create(T object) {
		return new Singleton<T>(object);
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
	
	private Singleton(T object) {
		value=object;
	}
	
	public PersistentList<T> front() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public PersistentList<T> back() {
		return Tuple.EMPTY;
	}
}
