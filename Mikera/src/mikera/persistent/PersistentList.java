package mikera.persistent;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mikera.persistent.impl.Singleton;

public abstract class PersistentList<T> extends PersistentCollection<T> implements IPersistentList<T> {
	private static final long serialVersionUID = -7221238938265002290L;

	public abstract T get(int i);
	
	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	
	
	private class BaseIterator implements ListIterator<T> {
		int i=0;
		
		public void add(T e) {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return (i<size());
		}

		public boolean hasPrevious() {
			return i>0;
		}

		public T next() {
			return get(i++);
		}

		public int nextIndex() {
			int s=size();
			return (i<s)?i+1:s;
		}

		public T previous() {
			return get(--i);
		}

		public int previousIndex() {
			return i-1;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(T e) {
			throw new UnsupportedOperationException();
		}	
	}

	public ListIterator<T> listIterator() {
		return new BaseIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return new BaseIterator();
	}

	public Iterator<T> iterator() {
		return new BaseIterator();
	}

	
	/**
	 * Returns the front part of the list. Not guaranteed to be exactly half,
	 * but intended to be as balanced as possible
	 * @return
	 */
	public abstract PersistentList<T> front();

	/**
	 * Returns the back part of the list. Not guaranteed to be exactly half,
	 * but intended to be as balanced as possible
	 * @return
	 */
	public abstract PersistentList<T> back();


	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	public PersistentList<T> append(T value) {
		return ListFactory.concat(this,Singleton.create(value));
	}

	public PersistentList<T> append(PersistentList<T> values) {
		return ListFactory.concat(this,values);
	}
	
	public PersistentList<T> append(Collection<T> values) {
		return ListFactory.concat(this,ListFactory.create(values));
	}
	
	public int indexOf(Object o) {
		int i=0;
		for (T it: this) {
			if (it!=null) {
				if (it.equals(o)) return i;
			} else {
				if (o==null) return i;
			}
			i++;
		}
		return -1;
	}
	
	public int indexOf(Object o, int start) {
		int i=start;
		while(i<size()) {
			T it=get(i);
			if (it!=null) {
				if (it.equals(o)) return i;
			} else {
				if (o==null) return i;
			}
			i++;
		}
		return -1;
	}
	
	public PersistentList<T> delete(int start, int end) {
		if ((start<0)||(end>size())) throw new IndexOutOfBoundsException();
		if (start>=end) {
			if (start>end) throw new IllegalArgumentException();
			return this;
		}
		if (start==0) return subList(end,size());
		if (end==size()) return subList(0,start);
		return subList(0,start).append(subList(end,size()));
	}


	public PersistentList<T> subList(int fromIndex, int toIndex) {
		return ListFactory.create(this,fromIndex,toIndex);
	}

	public PersistentList<T> update(int index, T value) {
		return subList(0,index).append(value).append(subList(index+1,size()));
	}

	public PersistentList<T> insert(int index, T value) {
		return subList(0,index).append(value).append(subList(index,size()));
	}

	public PersistentList<T> insert(int index, Collection<T> values) {
		if (values instanceof PersistentList<?>) {
			return subList(0,index).append(values).append(subList(index,size()));
		}
		PersistentList<T> pl=ListFactory.create(values);
		return subList(0,index).append(pl).append(subList(index,size()));
	}
	
	public PersistentList<T> delete(int index) {
		return delete(index,index+1);
	}


}
