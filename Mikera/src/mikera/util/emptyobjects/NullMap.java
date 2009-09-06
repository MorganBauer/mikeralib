package mikera.util.emptyobjects;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class NullMap<K,V> implements Map<K, V> {
	private NullMap() {
		
	}

	public void clear() {
		
	}

	public boolean containsKey(Object key) {
		return false;
	}

	public boolean containsValue(Object value) {
		return false;
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return null;
	}

	public V get(Object key) {
		return null;
	}

	public boolean isEmpty() {
		return true;
	}

	public Set<K> keySet() {
		return new NullSet<K>();
	}

	public V put(K key, V value) {
		return null;
	}

	public void putAll(Map<? extends K, ? extends V> m) {

	}

	public V remove(Object key) {
		return null;
	}

	public int size() {
		return 0;
	}

	public Collection<V> values() {
		return new NullSet<V>();
	}
}
