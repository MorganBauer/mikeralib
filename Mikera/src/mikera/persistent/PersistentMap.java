package mikera.persistent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import mikera.util.Tools;

public abstract class PersistentMap<K,V> extends PersistentObject implements IPersistentMap<K,V> {
	private static final long serialVersionUID = 2304218229796144868L;

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public abstract boolean containsKey(Object arg0);

	public boolean containsValue(Object value) {
		for (Map.Entry<K,V> ent: entrySet()) {
			if (Tools.equalsWithNulls(ent.getValue(),value)) {
				return true;
			}
		}
		return false;
	}

	public abstract PersistentSet<java.util.Map.Entry<K, V>> entrySet();

	public abstract V get(Object key);

	public boolean isEmpty() {
		return (size()>0);
	}

	public abstract PersistentSet<K> keySet();

	public V put(K arg0, V arg1) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends K, ? extends V> arg0) {
		throw new UnsupportedOperationException();
	}

	public V remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public abstract int size();

	public abstract PersistentCollection<V> values();

	@SuppressWarnings("unchecked")
	public PersistentMap<K,V> clone() {
		return (PersistentMap<K,V>)super.clone();
	}
	
	
	public abstract PersistentMap<K, V> delete(K key);

	public PersistentMap<K, V> delete(Collection<K> keys) {
		PersistentMap<K, V> pm=this;
		for (K k: keys) {
			pm=pm.delete(k);
		}
		return pm;
	}

	public PersistentMap<K, V> delete(PersistentSet<K> keys) {
		return delete((Collection<K>) keys);
	}

	public abstract PersistentMap<K, V> include(K key, V value);

	public PersistentMap<K, V> include(Map<K, V> values) {
		PersistentMap<K, V> pm=this;
		for (Map.Entry<K, V> entry:values.entrySet()) {
			pm=pm.include(entry.getKey(),entry.getValue());
		}
		return pm;
	}

	public PersistentMap<K, V> include(PersistentMap<K, V> values) {
		return include((Map<K,V>) values);
	}
}
