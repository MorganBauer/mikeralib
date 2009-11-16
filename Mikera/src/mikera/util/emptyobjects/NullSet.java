package mikera.util.emptyobjects;

import java.util.*;

import mikera.persistent.*;
import mikera.persistent.impl.FilteredIterator;
import mikera.util.Tools;

public final class NullSet<T> extends PersistentSet<T> {
	private static final long serialVersionUID = -6170277533575154354L;
	
	@SuppressWarnings("unchecked")
	public static NullSet<?> INSTANCE=new NullSet();
	
	private NullSet() {
		
	}
	
	@Override
	public boolean contains(Object t) {
		return false;
	}

	@Override
	public PersistentSet<T> include(T value) {
		return SetFactory.create(value);
	}

	@Override
	public int size() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return (Iterator<T>) NullIterator.INSTANCE;
	}
	
	public PersistentSet<T> deleteAll(final T value) {
		return this;
	}

	public PersistentSet<T> deleteAll(final Collection<T> values) {
		return this;
	}
	
	@Override
	public PersistentSet<T> include(final Collection<T> values) {
		return SetFactory.create(values);
	}
	
	public PersistentSet<T> include(final PersistentSet<T> values) {
		return SetFactory.create(values);
	}
}
