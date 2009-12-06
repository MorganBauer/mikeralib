package mikera.util;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

import mikera.annotations.Mutable;
import mikera.util.emptyobjects.NullArrays;

@Mutable
public final class RankedQueue<T> extends AbstractQueue<T>{

	private int size;
	
	@SuppressWarnings("unchecked")
	private T[] objects=(T[])NullArrays.NULL_OBJECTS;
	private double[] ranks=NullArrays.NULL_DOUBLES;
	
	
	protected class RankedQueueIterator implements Iterator<T> {
		int i;
		
		public RankedQueueIterator() {
			i=0;
		}
		
		public void add(T e) {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return (i<size());
		}

		public T next() {
			return getObjectAtIndex(i++);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(T e) {
			throw new UnsupportedOperationException();
		}			
	}
	
	@SuppressWarnings("unchecked")
	protected void ensureCapacity(int s) {
		int currentCapacity=objects.length;
		if (s<=currentCapacity) return;
		int newSize=Math.max(s, currentCapacity*2+10);
		double[] nr=new double[newSize];
		T[] no=(T[]) new Object[newSize];
		
		System.arraycopy(objects, 0, no, 0, size);
		System.arraycopy(ranks, 0, nr, 0, size);
		
		objects=no;
		ranks=nr;
	}
	
	public int size() {
		return size;
	}
	
	private T getObjectAtIndex(int i) {
		return objects[i];
	}
	
	@Override
	public Iterator<T> iterator() {
		return new RankedQueueIterator();
	}

	public boolean offer(T e) {
		throw new UnsupportedOperationException();
	}

	public T peek() {
		if (size==0) return null;
		return objects[0];
	}
	
	public double peekRank() {
		if (size==0) throw new NoSuchElementException();
		return ranks[0];
	}
	
	@Override
	public T element() {
		if (size==0) throw new NoSuchElementException();
		return objects[0];
	}
	
	@Override
	public boolean isEmpty() {
		return (size==0);
	}
	
	//*******************************
	// functions to calcuilate parent and child indexes for any given position
	//
	
	public static final int child1(int i) {
		return ((i+1)<<1)-1;
	}
	
	public static final int child2(int i) {
		return ((i+1)<<1);
	}
	
	public static final int parent(int i) {
		return ((i-1)>>1);
	}

	public T poll() {
		if (size==0) return null;
		T result=objects[0];
		
		if (size==1) {
			size=0;
		} else {
			// get the object to place
			T to=objects[size-1];
			double tr=ranks[size-1];
			size--;
			
			int i=0;
			int ch=child1(i);
			int cc=size-ch; // number of child elements to consider
			while (cc>0) {
				// advance if needed to point at lowest ranked child
				if ((cc>1)&(ranks[ch]>ranks[ch+1])) ch++;
				
				// check if we are correctly ranked at position i
				if (tr<ranks[ch]) break;
				
				// move child down
				objects[i]=objects[ch];
				ranks[i]=ranks[ch];
				
				// update indexes
				i=ch;
				ch=child1(i);
				cc=size-ch;
			}
			objects[i]=to;
			ranks[i]=tr;
		}
		
		return result;
	}
	


	@Override
	public boolean add(T o) {
		add(o,0.0);
		return true;
	}
	
	protected void swap(int ai, int bi) {
		T to=objects[ai];
		objects[ai]=objects[bi];
		objects[bi]=to;
		
		double td=ranks[ai];
		ranks[ai]=ranks[bi];
		ranks[bi]=td;
		
	}
	
	public void percolate(int i) {
		T o=objects[i];
		double rank=ranks[i];
		while (i!=0) {
			int pi=parent(i);
			double prank=ranks[pi];
			if (prank<=rank) break;
			objects[i]=objects[pi];
			ranks[i]=prank;
			i=pi;
		}
		objects[i]=o;
		ranks[i]=rank;
	}
	
	public void add(T o, double d) {
		ensureCapacity(size+1);
		objects[size]=o;
		ranks[size]=d;
		size++;
		percolate(size-1);
	}

	public void validate() {
		for (int i=0; i<size; i++) {
			int ch=child1(i);
			if ((ch<size)&&(ranks[i]>ranks[ch])) throw new Error();
			ch++;
			if ((ch<size)&&(ranks[i]>ranks[ch])) throw new Error();
		}
	}

}
