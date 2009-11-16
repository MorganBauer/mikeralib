package mikera.persistent;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mikera.persistent.impl.BasePersistentSet;
import mikera.persistent.impl.KeySetWrapper;
import mikera.persistent.impl.ValueCollectionWrapper;
import mikera.util.Tools;

/**
 * Persistent HashMap implementation, inspired by Clojure's
 * persistent hash map data structures.
 * 
 * @author Mike Anderson
 *
 * @param <K> Key type
 * @param <V> Value type
 */

public final class PersistentHashMap<K,V> extends PersistentMap<K,V> {
	private static final long serialVersionUID = -6862000512238861885L;

	private PHMNode<K,V> root;

	@SuppressWarnings("unchecked")
	private static final PHMEntryList EMPTY_NODE_LIST=new PHMEntryList(new PHMEntry[0]);
	
	@SuppressWarnings("unchecked")
	public PersistentHashMap() {
		this(EMPTY_NODE_LIST);
	}
	
	public PersistentHashMap(PHMNode<K,V> newRoot) {
		root=newRoot;
	}
	
	@SuppressWarnings("unchecked")
	public static<K,V> PersistentHashMap<K,V> create(K key, V value) {
		return new PersistentHashMap(new PHMEntry(key,value));
	}
	
	@SuppressWarnings("unchecked")
	public static<K,V> PersistentHashMap<K,V> create(Map<K,V> values) {
		PersistentHashMap pm=new PersistentHashMap();
		for (Map.Entry<K,V> ent: values.entrySet()) {
			pm=(PersistentHashMap) pm.include(ent.getKey(),ent.getValue());
		}
		return pm;
	}
	
	
	private abstract static class PHMNode<K,V> extends PersistentObject {
		/**
		 * Removes key from HashNode, returning a modified HashNode
		 * @param key
		 * @return Modified HashNode, or the same HashNode if key not found
		 */
		protected abstract PHMNode<K,V> delete(K key);

		protected abstract PHMNode<K,V> include(K key, V value);
		
		protected abstract PHMEntry<K,V> getEntry(K key);
		
		protected abstract PHMEntry<K,V> findNext(PHMEntrySetIterator<K,V> it);
		
		protected abstract int size();
		
		public final boolean containsKey(K key) {
			return getEntry(key)!=null;
		}

	}
	
	private static final class PHMEntryList<K,V> extends PHMNode<K,V> {
		private final PHMEntry<K,V>[] entries;
		
		public PHMEntryList(PHMEntry<K,V>[] list) {
			entries=list;
		}

		@Override
		protected PHMEntry<K, V> getEntry(K key) {
			for (PHMEntry<K,V> ent : entries) {
				if (ent.matches(key)) return ent;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected PHMNode<K, V> include(K key, V value) {
			int pos=-1;
			for (int i=0; i<entries.length; i++) {
				PHMEntry<K,V> ent=entries[i];
				if (ent.matches(key)) {
					if (ent.matchesValue(value)) return this;
					pos=i;
					break;
				}
			}
			int olen=entries.length;
			int nlen=olen+( (pos>=0)?0:1 );
			PHMEntry<K,V>[] ndata=new PHMEntry[nlen];
			System.arraycopy(entries, 0, ndata, 0, entries.length);
			if (pos>=0) {
				ndata[pos]=new PHMEntry<K,V>(key,value);
			} else {
				ndata[olen]=new PHMEntry<K,V>(key,value);
			}
			return new PHMEntryList(ndata);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected PHMNode<K, V> delete(K key) {
			int pos=-1;
			int len=entries.length;
			for (int i=0; i<len; i++) {
				PHMEntry<K,V> ent=entries[i];
				if (ent.matches(key)) {
					pos=i;
				}
			}
			if (pos<0) return this;
			if (len==2) {
				return entries[1-pos]; // return other entry
			}
			PHMEntry<K,V>[] ndata=new PHMEntry[len-1];
			System.arraycopy(entries,0,ndata,0,pos);
			System.arraycopy(entries,pos+1,ndata,pos,len-pos-1);
			return new PHMEntryList<K,V>(ndata);
		}

		@Override
		protected int size() {
			return entries.length;
		}

		@Override
		protected PHMEntry<K, V> findNext(PHMEntrySetIterator<K, V> it) {
			if (it.index>=entries.length) {
				return null;
			} else {
				return entries[it.index++];
			}
		}
	}
	
	private static final class PHMEntry<K,V> extends PHMNode<K,V> implements Map.Entry<K, V> {
		private int hashCode;
		private final K key;
		private final V value;
		
		public K getKey() {
			return key;
		}
		
		public V getValue() {
			return value;
		}
		
		public int getHashCode() {
			return hashCode;
		}
		
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
		
		
		public PHMEntry(K k, V v) {
			key=k;
			value=v;
			hashCode=k.hashCode();
		}
		
		public boolean matches(K key) {
			return this.key.equals(key);
		}
		
		public boolean matchesValue(V value) {
			return this.value==value;
		}
		
		@Override
		protected PHMEntry<K, V> getEntry(K key) {
			if (matches(key)) return this;
			return null;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected PHMNode<K, V> include(K newkey, V value) {
			if (newkey.equals(this.key)) {
				// replacement case
				if (!matchesValue(value)) return new PHMEntry<K,V>(newkey,value);
				return this;
			}
			// TODO: return HashNode with two entries 
			return new PHMEntryList<K,V>(
					new PHMEntry[] {
							this,
							new PHMEntry<K,V>(newkey,value)});
		}
		
		@Override
		protected PHMNode<K, V> delete(K key) {
			if (key.equals(key)) return null;
			return this;
		}
		
		@Override
		protected int size() {
			return 1;
		}

		@Override
		protected PHMEntry<K,V> findNext(PHMEntrySetIterator<K, V> it) {
			if (it.index>0) {
				return null;
			} else {
				it.index=1;
				return this;
			}
		}
	}
	
	/**
	 * EntrySet implementation
	 */
	protected class PHMEntrySet extends PersistentSet<Map.Entry<K,V>> implements Set<Map.Entry<K,V>> {
		@Override
		public int size() {
			return PersistentHashMap.this.size();
		}
		
		@SuppressWarnings("unchecked")
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry<?,?>)) return false;
			Map.Entry<?,?> ent=(Map.Entry<?,?>)o;
			PHMEntry pe=PersistentHashMap.this.getEntry((K)ent.getKey());
			if (pe==null) return false;
			return Tools.equalsWithNulls(pe.value, ent.getValue());
		}

		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new PHMEntrySetIterator<K,V>(PersistentHashMap.this);
		}

		public PersistentSet<java.util.Map.Entry<K, V>> include(
				java.util.Map.Entry<K, V> value) {
			return SetFactory.create(this).include(value);
		}
	}
	
	
	/**
	 * Entry set iterator
	 * @author Mike
	 *
	 * @param <K>
	 * @param <V>
	 */
	private static class PHMEntrySetIterator<K,V> implements Iterator<Map.Entry<K,V>> {
		public PersistentHashMap<K,V> hashMap;
		public PHMEntry<K,V> next;
		public int hash=Integer.MIN_VALUE;
		public int index=0;
		
		private PHMEntrySetIterator(PersistentHashMap<K,V> phm) {
			hashMap=phm;
			findNext();
		}

		public boolean hasNext() {
			return (next!=null);
		}

		public java.util.Map.Entry<K, V> next() {
			Map.Entry<K, V> result=next;
			findNext();
			return result;
		}
		
		private void findNext() {
			next=hashMap.root.findNext(this);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/*
	 *  IPersistentMap methods
	 */

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(Object key) {
		return root.containsKey((K)key);
	}

	@Override
	public PersistentSet<java.util.Map.Entry<K, V>> entrySet() {
		return new PHMEntrySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		PHMEntry<K,V> entry=root.getEntry((K)key);
		if (entry!=null) return entry.getValue();
		return null;
	}
	
	public PHMEntry<K,V> getEntry(K key) {
		return root.getEntry(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersistentSet<K> keySet() {
		return new KeySetWrapper((PersistentSet<?>) entrySet());
	}

	@Override
	public int size() {
		return root.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersistentCollection<V> values() {
		return new ValueCollectionWrapper((PersistentSet<?>) entrySet());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersistentMap<K, V> include(K key, V value) {
		PHMNode<K,V> newRoot=root.include(key, value);
		if (root==newRoot) return this;
		return new PersistentHashMap(newRoot);
	}
	
	@Override
	public PersistentMap<K, V> include(Map<K, V> values) {
		// TODO: faster implementation for PersistentHashMap merges
		PersistentMap<K, V> pm=this;
		for (Map.Entry<K, V> entry:values.entrySet()) {
			pm=pm.include(entry.getKey(),entry.getValue());
		}
		return pm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersistentMap<K, V> delete(K key) {
		PHMNode<K,V> newRoot=root.delete(key);
		if (root==newRoot) return this;
		return new PersistentHashMap(newRoot);
	}

}
