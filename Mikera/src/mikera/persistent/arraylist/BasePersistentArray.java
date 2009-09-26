package mikera.persistent.arraylist;

import java.io.Serializable;
import java.util.*;

import mikera.persistent.PersistentArray;

@SuppressWarnings("serial")
public class BasePersistentArray<T> implements PersistentArray<T> {

	public BasePersistentArray<T> clone() {
		return this;
	}

	public PersistentArray<T> append(T value) {
		return null;
	}

	public PersistentArray<T> append(PersistentArray<T> value) {
		return CompositeArray.concat(this,value);
	}

	public int end() {
		throw new UnsupportedOperationException();
	}

	public T get(int i) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public int start() {
		throw new UnsupportedOperationException();
	}
}
