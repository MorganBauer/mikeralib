package mikera.persistent;

import java.util.Set;

public class PersistentHashMap<K,V> extends PersistentMap<K,V> {

	private HashNode root;

	
	private abstract static class HashNode {
		
	}
	
	// TODO: Update with some data structure!
	
	//private abstract static class SingleHashNode {
	//	int hashcode;
	//	K key;
	//	V value;
	//}
	
	
	@Override
	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PersistentCollection values() {
		// TODO Auto-generated method stub
		return null;
	}

}
