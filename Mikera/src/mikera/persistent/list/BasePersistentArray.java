package mikera.persistent.list;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import mikera.persistent.PersistentCollection;
import mikera.persistent.PersistentList;

@SuppressWarnings("serial")
public class BasePersistentArray<T> implements PersistentList<T> {

	public BasePersistentArray<T> clone() {
		return this;
	}

	public PersistentList<T> append(T value) {
		return null;
	}

	public PersistentList<T> append(PersistentList<T> value) {
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

	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		return indexOf(o)>=0;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object it: c) {
			if (!contains(it)) return false;
		}
		return true;
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

	public boolean isEmpty() {
		return size()>0;
	}

	public Iterator<T> iterator() {
		return new BaseIterator();
	}

	public int lastIndexOf(Object o) {
		int i=0;
		int res=-1;
		for (T it: this) {
			if (it!=null) {
				if (it.equals(o)) res=i;
			} else {
				if (o==null) res=i;
			}
			i++;
		}
		return res;
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

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
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

	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] toArray() {
		Object[] os=new Object[size()];
		int i=0;
		for (T it: this) {
			os[i++]=it;
		}
		return os;
	}

	@SuppressWarnings("unchecked")
	public <V> V[] toArray(V[] a) {
		int size=size();
		if (a.length<size) {
			a=(V[]) Array.newInstance(a.getClass().getComponentType(), size);
		}
		int i=0;
		for (T it: this) {
			a[i++]=(V)it;
		}
		return a;
	}
	
	/**
	 * Returns hashcode of the persistent array. Defined as XOR of all elements rotated right for each element
	 */
	public int hashCode() {
		return 0;
	}

	public PersistentList<T> delete(int index) {
		throw new UnsupportedOperationException();
	}

	public PersistentList<T> delete(int start, int end) {
		throw new UnsupportedOperationException();
	}

	public PersistentList<T> deleteFirst(T value) {
		throw new UnsupportedOperationException();
	}
	
	public PersistentList<T> deleteAll(T value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PersistentList<T> deleteAll(PersistentCollection<T> values) {
		// TODO Auto-generated method stub
		return null;
	}
}
