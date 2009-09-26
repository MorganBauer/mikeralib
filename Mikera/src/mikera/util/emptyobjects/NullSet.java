package mikera.util.emptyobjects;

import java.util.*;

import mikera.persistent.*;

public class NullSet<T> implements Collection<T>, PersistentSet<T> {
	
	@SuppressWarnings("unchecked")
	public static NullSet<?> INSTANCE=new NullSet();
	
	private NullSet() {
		
	}
	
	public boolean add(T e) {
		throw new Error("Cannot add to NullSet");
	}

	public boolean addAll(Collection<? extends T> c) {
		return false;
	}

	public void clear() {

	}

	public boolean contains(Object o) {
		return false;
	}

	public boolean containsAll(Collection<?> c) {
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
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
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
