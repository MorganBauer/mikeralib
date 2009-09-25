package mikera.util.emptyobjects;

import java.util.Iterator;

public class NullIterator<T> implements Iterator<T> {
	public boolean hasNext() {
		return false;
	}

	public T next() {
		return null;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
