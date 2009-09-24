package mikera.util;
import java.util.*;

/**
 * Indexed list class <K,V>
 * 
 * Implemented using binary searchable key array 
 * and value array of same size
 * 
 * @author Mike
 *
 * @param <K>
 * @param <V>
 */
public class IndexedList<K extends Comparable<K>,V> implements Map<K,V> {

	private Comparator<K> comparator=null;
	
	private static final int INITIAL_SIZE=10;
	private static final float GROW_RATIO=1.5f;
	
	private Object[] keys=new Object[INITIAL_SIZE];
	private Object[] values=new Object[INITIAL_SIZE];
	
	private int count=0;
	
	public IndexedList() {
	}
	
	public IndexedList(Comparator<K> c) {
		comparator=c;
	}
	
	private void ensureSize(int i) {
		int currentLength=keys.length;
		if (i<=currentLength) return;
		
		int nl=Math.min(i,(int)(currentLength*GROW_RATIO));
		
		Object[] newKeys=new Object[nl];
		Object[] newValues=new Object[nl];
		
		System.arraycopy(keys, 0, newKeys, 0, count);
		System.arraycopy(values, 0, newValues, 0, count);
		
		values=newValues;
		keys=newKeys;
	}
	
	// shift entries, assumes all bounds checked
	private void copyEntries(int src, int dst, int num) {
		/*
		  if (src>dst) {

			for (int i=0; i<num; i++) {
				keys[dst+i]=keys[src+i];
				values[dst+i]=values[src+i];
			}
		} else {
			for (int i=num-1; i>=0; i--) {
				keys[dst+i]=keys[src+i];
				values[dst+i]=values[src+i];
			}
		}
		*/
		System.arraycopy(keys, src, keys, dst, num);
		System.arraycopy(values, src, values, dst, num);
	}
	
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		int i=findIndex((K)key);
		if (i<0) return null;
		return (V)values[i];
	}
	
	/**
	 * 
	 * @param key
	 * @return index of key value, or -1 if not found;
	 */
	@SuppressWarnings("unchecked")
	public int findIndex(K key) {
		int max=count;
		int min=0;
		
		while (min<max) {
			int i=(max+min)>>1;
			int c=compare(key,(K)keys[i]);
			if (c==0) return i;
			if (c<0) {
				max=i;
			} else {
				min=i+1;
			}
		}
		return -1;
	}
	
	public int findValueIndex(V value) {
		for (int i=0; i<count; i++) {
			if (value.equals(values[i])) return i;
		}
		return -1;
	}
	
	public void clear() {
		count=0;
		int kl=keys.length;
		if (kl>INITIAL_SIZE) {
			keys=new Object[INITIAL_SIZE];
			values=new Object[INITIAL_SIZE];	
		} else {
			for (int i=0; i<kl; i++) {
				keys[i]=null;
				values[i]=null;
			}
		}
	}
	
	public boolean isEmpty() {
		return count<=0;
	}
	
	private int compare(K a, K b) {
		if (comparator!=null) {
			return comparator.compare(a, b);
		} else {
			return a.compareTo(b);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean containsKey(Object key) {
		return findIndex((K)key)>=0;
	}

	@SuppressWarnings("unchecked")
	public boolean containsValue(Object value) {
		return findValueIndex((V)value)>=0;
		
	}

	public Set<K> keySet() {
		return new StaticArrayList<K>(keys);
	}

	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		int max=count;
		int min=0;
		int i=0;
		
		while (min<max) {
			i=(max+min)>>1;
			int c=compare(key,(K)keys[i]);
			if (c==0) {
				V old=(V)values[i];
				values[i]=value;
				return old;
			}
			if (c<0) {
				max=i;
			} else {
				min=i+1;
			}
		}
		i=min;
		ensureSize(count+1);
		copyEntries(i,i+1,count-i);
		keys[i]=key;
		values[i]=value;
		count++;
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map<? extends K, ? extends V> map) {
		if (map instanceof IndexedList) {
			putAll(map);
			return;
		}
		
		Iterator it=map.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry<K, V> entry=(java.util.Map.Entry<K, V>)it.next();
			put(entry.getKey(),entry.getValue());
		}
	}
	
	public void setToClone(IndexedList<K,V> list) {
		keys=(Object[]) list.keys.clone();
		values=(Object[]) list.values.clone();
		count=list.count;
	}
	
	@SuppressWarnings("unchecked")
	public void putAll(IndexedList<K,V> list) {
		int ca=count;
		int cb=list.count;
		if (cb==0) return;
		if (ca==0) {
			setToClone(list);
			return;
		}
		
		int newSize=count+list.size();
		
		Object[] newKeys=new Object[newSize];
		Object[] newValues=new Object[newSize];
		
		int ia=0;
		int ib=0;
		int newCount=0;
		K a=null;
		K b=null;
		
		while (true) {
			if ((a==null)&&(ia<ca)) a=(K)keys[ia];
			if ((b==null)&&(ib<cb)) b=(K)list.keys[ib];
			int r=0; // 1=use a, 2=use b, 3=merge
			
			if (a==null) {
				if (b==null) break;
				r=2;
			} else {
				if (b==null) r=1;
			}
			if (r==0) {	
				int c=compare(a,b);
				if (c==0) r=3;
				else if (c<0) r=1;
				else r=2;
			}
			switch(r) {
				case 1: {
					newKeys[newCount]=a;
					newValues[newCount]=values[ia];
					ia++;
					a=null;
				}
				case 2: {
					newKeys[newCount]=b;
					newValues[newCount]=list.values[ib];
					ib++;
					b=null;
				}
				case 3: {
					newKeys[newCount]=b;
					newValues[newCount]=list.values[ib];
					ia++;
					ib++;
					a=null;
					b=null;
				}
			}
			newCount++;
		}
		
		keys=newKeys;
		values=newValues;
		count=newCount;
	}

	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		int i=findIndex((K)key);
		if (i<0) return null;
		
		V value=(V)values[i];
		if (i+1<count) {
			copyEntries(i+1,i,count-(i+i));
		}
		count--;
		return value;
	}

	public int size() {
		return count;
	}

	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		return new StaticArrayList<V>((V[])values);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new AbstractSet<java.util.Map.Entry<K, V>>() {

			@Override
			public Iterator<java.util.Map.Entry<K, V>> iterator() {
				return new Iterator<java.util.Map.Entry<K, V>>() {
					int pos=0;
					
					public boolean hasNext() {
						return pos<count;
					}

					public java.util.Map.Entry<K, V> next() {
						return new java.util.Map.Entry<K, V>() {
							int i=pos;
							
							@SuppressWarnings("unchecked")
							public K getKey() {
								return (K)keys[i];
							}

							@SuppressWarnings("unchecked")
							public V getValue() {
								return (V)values[i];
							}

							public V setValue(V value) {
								throw new UnsupportedOperationException();
							}
							
						};
					}

					public void remove() {
						// TODO Auto-generated method stub
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				return count;
			}			
		};
	}
}
