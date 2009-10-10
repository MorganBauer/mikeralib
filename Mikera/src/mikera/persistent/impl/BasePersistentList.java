package mikera.persistent.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import mikera.persistent.ListFactory;
import mikera.persistent.PersistentCollection;
import mikera.persistent.PersistentList;
import mikera.util.Maths;
import mikera.util.Tools;
import mikera.util.emptyobjects.NullList;

@SuppressWarnings("serial")
public abstract class BasePersistentList<T> extends BasePersistentCollection<T> implements PersistentList<T> {

	public BasePersistentList<T> clone() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public PersistentList<T> append(T value) {
		return ListFactory.concat(this,Singleton.create(value));
	}

	public PersistentList<T> append(PersistentList<T> values) {
		return ListFactory.concat(this,values);
	}

	public int end() {
		throw new UnsupportedOperationException();
	}



	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}



	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}



	public boolean contains(Object o) {
		return indexOf(o)>=0;
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


	
	/**
	 * Returns hashcode of the persistent array. Defined as XOR of hashcodes of all elements rotated right for each element
	 */
	@Override
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
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o instanceof List<?>) {
			return equals((List<T>)o);
		}
		return super.equals(o);
	}
	
	public boolean equals(List<T> pl) {
		int size=size();
		if (size!=pl.size()) return false;
		for (int i=0; i<size; i++) {
			if (!Tools.equalsWithNulls(get(i),pl.get(i))) return false;
		}
		return true;
	}

	public PersistentList<T> delete(int index) {
		return delete(index,index+1);
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

	public PersistentList<T> deleteFirst(T value) {
		int i=indexOf(value);
		if (i<0) return this;
		return delete(i,i+1);
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

	public PersistentList<T> update(int index, T value) {
		return subList(0,index).append(value).append(subList(index+1,size()));
	}

	public PersistentList<T> insert(int index, T value) {
		return subList(0,index).append(value).append(subList(index,size()));
	}

	public PersistentList<T> insert(int index, Collection<T> values) {
		if (values instanceof PersistentList<?>) {
			return subList(0,index).append((PersistentList<T>)values).append(subList(index,size()));
		}
		PersistentList<T> pl=ListFactory.create(values);
		return subList(0,index).append(pl).append(subList(index,size()));
	}
}
