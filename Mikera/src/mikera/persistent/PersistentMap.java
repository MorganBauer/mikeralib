package mikera.persistent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import mikera.util.Tools;

public abstract class PersistentMap<K,V> extends PersistentObject implements Map<K, V> {
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

	public abstract Set<java.util.Map.Entry<K, V>> entrySet();

	public abstract V get(Object key);

	public boolean isEmpty() {
		return (size()>0);
	}

	public abstract Set<K> keySet();

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

}
