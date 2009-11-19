package mikera.persistent;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import mikera.persistent.impl.FilteredIterator;
import mikera.util.Tools;

@SuppressWarnings("unchecked")
public abstract class PersistentCollection<T> extends PersistentObject implements IPersistentCollection<T> {
	private static final long serialVersionUID = -962303316004942025L;

	public abstract int size();
	
	public PersistentCollection<T> clone() {
		return (PersistentCollection<T>)super.clone();
	}
	
	public boolean isEmpty() {
		return size()==0;
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
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
			if (Tools.equalsWithNulls(it, o)) return true;
		}
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object it: c) {
			if (!contains(it)) return false;
		}
		return true;
	}
	
	public boolean containsAny(Collection<?> c) {
		for (Object it: c) {
			if (contains(it)) return true;
		}
		return false;
	}
	
	public boolean equals(Object o) {
		if (o instanceof PersistentCollection<?>) {
			return equals((PersistentCollection<T>)o);
		}
		return false;
	}
	
	public boolean equals(PersistentCollection<T> pm) {
		return this.containsAll(pm)&&pm.containsAll(this);
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

	public PersistentCollection<T> delete(final T value) {
		Iterator<T> it=new FilteredIterator<T>(iterator()) {
			@Override
			public boolean filter(Object testvalue) {
				return (!Tools.equalsWithNulls(value, testvalue));
			}		
		};
		return ListFactory.create(it);
	}
	
	public PersistentCollection<T> deleteAll(final PersistentCollection<T> values) {
		Iterator<T> it=new FilteredIterator<T>(iterator()) {
			@Override
			public boolean filter(Object value) {
				return (!values.contains(value));
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
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append('{');
		boolean first=true;
		for (T t: this) {
			if (first) {
				first=false;
			} else {
				sb.append(", ");
			}
			sb.append(t.toString());
		}		
		sb.append('}');
		return sb.toString();
	}
	
	public abstract PersistentCollection<T> include(final T value);
	
	public PersistentCollection<T> includeAll(final Collection<T> values) {
		PersistentCollection<T> ps=this;
		for (T t: values) {
			ps=ps.include(t);
		}
		return ps;
	}
	
	public PersistentCollection<T> includeAll(final PersistentCollection<T> values) {
		return includeAll((Collection<T>)values);
	}
	
	public void validate() {
		
	}
}
