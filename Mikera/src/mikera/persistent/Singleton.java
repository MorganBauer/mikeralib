package mikera.persistent;

import mikera.persistent.list.BasePersistentArray;

public final class Singleton<T> extends BasePersistentArray<T> {
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
}
