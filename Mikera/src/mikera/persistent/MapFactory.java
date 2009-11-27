package mikera.persistent;

import java.util.Map;

import mikera.util.emptyobjects.NullMap;

public class MapFactory {
	@SuppressWarnings("unchecked")
	public static <K,V> PersistentMap<K,V> create() {
		return NullMap.INSTANCE;
	}
	
	public static <K,V> PersistentMap<K,V> create(K key, V value) {
		return PersistentHashMap.create(key, value);
	}
	
	public static <K,V> PersistentMap<K,V> create(Map<K,V> values) {
		return PersistentHashMap.create(values);
	}
}
