package mikera.persistent;

import java.util.*;

import mikera.persistent.impl.BasePersistentCollection;
import mikera.util.Arrays;
import mikera.util.HashCache;

/**
 * Immutable small set of integers, stored as a sorted array
 * 
 * Should never contain duplicates
 * 
 * @author Mike
 *
 */
public final class IntSet extends BasePersistentCollection<Integer> {

	private static final long serialVersionUID = 2677550392326589873L;
	public static final IntSet EMPTY_SET=new IntSet(mikera.util.Arrays.NULL_INTS);
	private static final HashCache<IntSet> cache=new HashCache<IntSet>(401);

	/**
	 * data field contains an ordered list of unique integers
	 */
	private final int[] data;
		
	private IntSet(int[] values) {
		data =values;
	}
	
	public boolean contains(IntSet a) {
		int[] adata=a.data;
		int[] sdata=data;
		int ai=0;
		int si=0;
		while (ai<adata.length) {
			int needed=adata[ai++];
			while (true) {
				int s=sdata[si++];
				if (s>needed) return false; // not found
				if (s==needed) break;
				if (si>=sdata.length) return false;
			}
		}
		return true;
	}
	
	/**
	 * Testing method to check for duplicates, should always be false
	 * 
	 * @return
	 */
	public boolean hasDuplicates() {
		for (int i=0; i<data.length-1; i++) {
			if (data[i]==data[i+1]) return true;
		}
		return false;
	}

	public boolean contains (int v) {
		return findIndex(v,0,data.length)>=0;
	}
	
	public int[] toIntArray() {
		return (int[])data.clone();
	}
	
	public int findIndex(int v) {
		return findIndex(v,0,data.length);
	}

	public int findIndex(int v, int lo, int hi) {
		while (lo<hi) {
			int m=(lo+hi)>>>1;
			int dv=data[m];
			if (dv==v) return m;
			if (dv<v) {
				lo = m+1;
			} else {
				hi = m;
			}
		} 
		return -1;
	}
	
	public static IntSet create(int value) {
		int hc=hashCode(value);
		IntSet is=cache.getCachedValueForHashCode(hc);
		if ((is!=null)&&(is.size()==1)&&(is.data[0]==value)) return is;
		return createLocal(new int[] {value});
	}
	


	public static IntSet create(int[] data) {
		return create(data,0,data.length);
	}

	public static IntSet createMerged(IntSet a, IntSet b) {
		int ai=0;
		int bi=0;
		int nsize=0;
		while ((ai<a.data.length)&&(bi<b.data.length)) {
			int c=a.data[ai]-b.data[bi];
			if (c>0) {
				bi++;
			} else if (c<0) {
				ai++;
			} else {
				ai++;
				bi++;
			}
			nsize++;
		}
		nsize+=a.data.length+b.data.length-ai-bi;	
		int[] ndata=new int[nsize];
		ai=0;
		bi=0;
		for (int i=0; i<nsize; i++) {
			if (ai>=a.data.length) {ndata[i]=b.data[bi++]; continue;}
			if (bi>=b.data.length) {ndata[i]=a.data[ai++]; continue;}
			int c=a.data[ai]-b.data[bi];
			if (c>0) {
				ndata[i]=b.data[bi++];
			} else if (c<0) {
				ndata[i]=a.data[ai++];
			} else {
				ndata[i]=a.data[ai++];
				bi++;
			}
		}
		return createLocal(ndata);
	}
	
	public static IntSet createMerged(IntSet is, int v) {
		if (is.contains(v)) return is;
		int[] data=is.data;
		int ol=data.length;
		int[] ndata=new int[ol+1];
		int i=0;
		while ((i<ol)) {
			int dv=data[i];
			if (dv>v) {
				break;
			}
			ndata[i++]=dv;
		}
		ndata[i++]=v;
		while (i<=ol) {
			ndata[i]=data[i-1];
			i++;
		}
		return createLocal(ndata);
	}
	
	public static IntSet createWithout(IntSet is, int v) {
		int pos=is.findIndex(v);
		if (pos<0) return is; // no removal
		int[] data=is.data;
		int ol=data.length;
		int[] ndata=new int[ol-1];
		System.arraycopy(data, 0, ndata, 0, pos);
		System.arraycopy(data, pos+1, ndata, pos, ol-pos-1);
		return createLocal(ndata);
	}
	
	private static IntSet create(int[] data, int offset, int size) {
		if (size==0) return EMPTY_SET;
		int[] ndata=new int[size];
		System.arraycopy(data, offset, ndata, 0, size);
		java.util.Arrays.sort(ndata);
		ndata=Arrays.deduplicate(ndata);
		return createLocal(ndata);
	}
		
	private static IntSet createLocal(int[] sortedData) {
		return intern(new IntSet(sortedData));
	}
	
	public static IntSet intern(IntSet is) {
		is=cache.cache(is);
		return is;
	}

	public int hashCode() {
		return hashCode(data);
	}
	
	/**
	 * Hash code based on summed hash codes of individual integer values
	 * 
	 * Defined as XOR of hashcodes of all elements rotated right for each element, to be consistent with PersistentList<T>
	 * 
	 * @param data
	 * @return
	 */
	public static int hashCode(int[] data) {
		int result=0;
		for(int i=0; i<data.length; i++) {
			result^=hashCode(data[i]);
			result=Integer.rotateRight(result, 1);
		}
		return result;
	}
	
	/**
	 * Hashcode for an int, defined as the value of the int itself for consistency with java.lang.Integer
	 * 
	 * @param value
	 * @return
	 */
	private static int hashCode(int value) {
		return value;
	}
	
	/**
	 * clone() returns the same IntSet, as it is defined to be immutable
	 */
	public IntSet clone() {
		return this;
	}
	
	public boolean equals(IntSet is) {
		if (is==this) return true;
		int s=size();
		if (is.size()!=s) return false;
		for (int i=0; i<s; i++) {
			if (data[i]!=is.data[i]) return false;
		}
		return true;
	}
	
	public boolean equals(Object o) {
		if ((o instanceof IntSet)) {
			return equals((IntSet) o);
		}
		return super.equals(o);
	}

	/**
	 * Set<Integer> methods
	 */
	public boolean add(Integer e) {
		throw new Error("IntSet is Immutable");
	}

	public boolean addAll(Collection<? extends Integer> c) {
		throw new Error("IntSet is Immutable");
	}

	public void clear() {
		throw new Error("IntSet is Immutable");
	}

	public boolean contains(Object o) {
		if (!(o instanceof Integer)) return false;
		
		return contains (((Integer)o).intValue());
	}

	public boolean containsAll(Collection<?> c) {
   		for (Object o: c) {
			if (!contains(o)) return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return size()==0;
	}

	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			int pos=0;
			
			public boolean hasNext() {
				return pos<size();
			}

			public Integer next() {
				return Integer.valueOf(data[pos++]);
			}

			public void remove() {
				throw new Error("IntSet is Immutable");
			}
		};
	}

	public boolean remove(Object o) {
		throw new Error("IntSet is Immutable");
	}

	public boolean removeAll(Collection<?> c) {
		throw new Error("IntSet is Immutable");
	}

	public boolean retainAll(Collection<?> c) {
		throw new Error("IntSet is Immutable");
	}

	public int size() {
		return data.length;
	}
	
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append('{');
		int s=size();
		for (int i=0; i<s; i++) {
			sb.append(data[i]);
			if ((i+1)<s) sb.append(", ");
		}		
		sb.append('}');
		return sb.toString();
	}

	public Object[] toArray() {
		int s=size();
		Integer[] ints= new Integer[s];
		for (int i=0; i<s; i++) {
			ints[i]=Integer.valueOf(data[i]);
		}
		return ints;
	}

	public <T> T[] toArray(T[] a) {
		throw new Error("Not supported");
	}


}
