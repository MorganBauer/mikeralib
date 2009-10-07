package mikera.persistent.list;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import mikera.persistent.ListFactory;
import mikera.persistent.PersistentCollection;
import mikera.persistent.PersistentList;
import mikera.persistent.Singleton;
import mikera.persistent.Tuple;
import mikera.util.Maths;
import mikera.util.Tools;
import mikera.util.emptyobjects.NullList;

@SuppressWarnings("serial")
public class BasePersistentArray<T> implements PersistentList<T> {

	public BasePersistentArray<T> clone() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public PersistentList<T> append(T value) {
		return ListFactory.concat(this,Singleton.create(value));
	}

	public PersistentList<T> append(PersistentList<T> value) {
		return ListFactory.concat(this,value);
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

	public PersistentList<T> subList(int fromIndex, int toIndex) {
		return ListFactory.create(this,fromIndex,toIndex);
	}
	
	public PersistentList<T> front() {
		return subList(0,size()/2);
	}

	public PersistentList<T> back() {
		return subList(size()/2,size());
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
	
	/**
	 * Returns hashcode of the persistent array. Defined as XOR of all elements rotated right for each element
	 */
	public int hashCode() {
		int result=0;
		for (int i=0; i<size(); i++) {
			Object v=get(i);
			if (v!=null) {
				result^=v.hashCode();
			}
			result=Integer.rotateRight(result, 1);
		}
		return result;
	}

	public PersistentList<T> delete(int index) {
		return ListFactory.concat(subList(0,index),subList(index+1,size()));
	}

	public PersistentList<T> delete(int start, int end) {
		return ListFactory.concat(subList(0,start),subList(end,size()));
	}

	public PersistentList<T> deleteFirst(T value) {
		return subList(1,size());
	}
	
	public PersistentList<T> deleteAll(T value) {
		PersistentList<T> pl=this;
		int i=pl.indexOf(value);
		while (i>=0) {
			pl=delete(i);
			i=pl.indexOf(value,i);
		}
		return pl;
	}

	public PersistentList<T> deleteAll(Collection<T> values) {
		PersistentList<T> pl=this;
		for (T t : values) { 
			pl=(PersistentList<T>) pl.deleteAll(t);
		}
		return pl;
	}

	public int compareTo(PersistentList<T> o) {
		int n=Maths.min(o.size(), size());
		for (int i=0; i<n; i++) {
			int r=Tools.compareWithNulls(this, o);
			if (r!=0) return r;
		}
		if (size()<o.size()) return -1;
		if (size()>o.size()) return 1;
		return 0;
	}


}
