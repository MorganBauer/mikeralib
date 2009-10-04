package mikera.util.emptyobjects;

import java.util.*;

import mikera.persistent.*;
import mikera.persistent.list.CompositeArray;

public class NullList<T> extends NullCollection<T> implements PersistentList<T> {
	@SuppressWarnings("unchecked")
	public static NullList<?> INSTANCE=new NullList();
	
	private NullList() {
		
	}

	public PersistentList<T> append(T value) {
		return Tuple.create(value);
	}

	public PersistentList<T> append(PersistentList<T> value) {
		return value;
	}

	public PersistentList<T> delete(int index) {
		return this;
	}

	public PersistentList<T> delete(int start, int end) {
		return this;
	}

	public PersistentList<T> deleteFirst(T value) {
		return this;
	}

	public T get(int i) {
		throw new IndexOutOfBoundsException();
	}

	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object o) {
		return -1;
	}

	public int lastIndexOf(Object o) {
		return -1;
	}

	@SuppressWarnings("unchecked")
	public ListIterator<T> listIterator() {
		return (ListIterator<T>) NullIterator.INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public ListIterator<T> listIterator(int index) {
		return (ListIterator<T>) NullIterator.INSTANCE;
	}

	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return this;
	}

	public int compareTo(PersistentList<T> o) {
		if (o.size()>0) return -1;
		return 0;
	}



}
