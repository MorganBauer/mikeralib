package mikera.persistent.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import mikera.persistent.ListFactory;
import mikera.persistent.PersistentCollection;
import mikera.util.Tools;

public abstract class BasePersistentCollection<T> implements PersistentCollection<T> {

	public PersistentCollection<T> deleteAll(final T value) {
		Iterator<T> it=new FilteredIterator<T>(iterator()) {
			@Override
			public boolean filter(Object testvalue) {
				return (!Tools.equalsWithNulls(value, testvalue));
			}		
		};
		return ListFactory.create(it);
	}

	public PersistentCollection<T> deleteAll(final Collection<T> values) {
		Iterator<T> it=new FilteredIterator<T>(iterator()) {
			PersistentCollection<T> col=ListFactory.create(values);
			
			@Override
			public boolean filter(Object value) {
				return (!col.contains(value));
			}		
		};
		return ListFactory.create(it);
	}


	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	
	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		for (T it: this) {
			if (it!=null) {
				if (it.equals(o)) return true;
			} else {
				if (o==null) return true;
			}
		}
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object it: c) {
			if (!contains(it)) return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return size()>0;
	}

	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		Object[] os=new Object[size()];
		int i=0;
		for (T it: this) {
			os[i++]=it;
		}
		return os;
	}

	public <V> V[] toArray(V[] a) {
		return toArray(a,0);
	}
	
	@SuppressWarnings("unchecked")
	public <V> V[] toArray(V[] a, int offset) {
		int size=size();
		if (a.length<(size+offset)) {
			a=(V[]) Array.newInstance(a.getClass().getComponentType(), size);
		}
		int i=0;
		for (T it: this) {
			a[offset+(i++)]=(V)it;
		}
		return a;
	}
	
	public BasePersistentCollection<T> clone() {
		return this;
	}
}
