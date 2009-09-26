package mikera.util.emptyobjects;

import java.util.*;

public class NullIterator<T> implements ListIterator<T> {
	public boolean hasNext() {
		return false;
	}

	public T next() {
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public void add(T e) {
		throw new UnsupportedOperationException();
	}

	public boolean hasPrevious() {
		return false;
	}

	public int nextIndex() {
		return 0;
	}

	public T previous() {
		throw new NoSuchElementException();
	}

	public int previousIndex() {
		return -1;
	}

	public void set(T e) {
		throw new UnsupportedOperationException();
	}

}
