package mikera.util.emptyobjects;

import java.util.*;

import mikera.persistent.*;

public class NullCollection<T> implements Collection<T>, PersistentCollection<T> {

	@SuppressWarnings("unchecked")
	public static NullCollection<?> INSTANCE=new NullCollection();
	
	protected NullCollection() {
		
	}
	
	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {

	}

	public boolean contains(Object o) {
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		if (c.isEmpty()) return true;
		return false;
	}

	public boolean isEmpty() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return (Iterator<T>)NullIterator.INSTANCE;
	}

	public boolean remove(Object o) {
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		return false;
	}

	public int size() {
		return 0;
	}

	public Object[] toArray() {
		return NullArrays.NULL_OBJECTS;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return (T[])toArray();
	}

	public PersistentCollection<T> deleteAll(T value) {
		return this;
	}

	public PersistentCollection<T> deleteAll(PersistentCollection<T> values) {
		return this;
	}


}
