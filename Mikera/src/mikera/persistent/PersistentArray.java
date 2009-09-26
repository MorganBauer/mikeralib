package mikera.persistent;

import java.io.Serializable;

public interface PersistentArray<T> extends Cloneable, Serializable {
	public T get(int i);
	
	public PersistentArray<T> append(T value);

	public PersistentArray<T> append(PersistentArray<T> value);
	
	public int start();
	public int size();
	public int end();

}
