package mikera.persistent;

import java.util.Map;
import java.util.Set;

import mikera.util.Tools;

/**
 * Persistent HashMap implementation, inspired by Clojure's
 * persistent hash map data structures.
 * 
 * @author Mike Anderson
 *
 * @param <K> Key type
 * @param <V> Vale type
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
	
	
	private abstract static class PHMNode<K,V> {
		/**
		 * Removes key from HashNode, returning a modified HashNode
		 * @param key
		 * @return Modified HashNode, or the same HashNode if key not found
		 */
		protected abstract PHMNode<K,V> delete(K key);

		protected abstract PHMNode<K,V> include(K key, V value);
		
		protected abstract PHMEntry<K,V> getEntry(K key);
		
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

		@Override
		protected PHMNode<K, V> delete(K key) {
			throw new Error("Not yet implemented!");
		}

		@Override
		protected int size() {
			return entries.length;
		}
	}
	
	private static final class PHMEntry<K,V> extends PHMNode<K,V> implements Map.Entry<K, V> {
		private int hashCode;
		private K key;
		private V value;
		
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
		protected PHMNode<K, V> include(K key, V value) {
			if (key.equals(key)) {
				// replacement case
				if (!matchesValue(value)) return new PHMEntry<K,V>(key,value);
				return this;
			}
			// TODO: return HashNode with two entries 
			return new PHMEntryList<K,V>(
					new PHMEntry[] {
							this,
							new PHMEntry<K,V>(key,value)});
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
	public Set<Map.Entry<K,V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		PHMEntry<K,V> entry=root.getEntry((K)key);
		if (entry!=null) return entry.getValue();
		return null;
	}

	@Override
	public PersistentSet<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return root.size();
	}

	@Override
	public PersistentCollection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersistentMap<K, V> include(K key, V value) {
		PHMNode<K,V> newRoot=root.include(key, value);
		if (root==newRoot) return this;
		return new PersistentHashMap(newRoot);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersistentMap<K, V> delete(K key) {
		PHMNode<K,V> newRoot=root.delete(key);
		if (root==newRoot) return this;
		return new PersistentHashMap(newRoot);
	}

}
