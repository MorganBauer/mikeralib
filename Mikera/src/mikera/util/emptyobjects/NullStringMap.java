package mikera.util.emptyobjects;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class NullStringMap implements Map<String, Object> {
	public static final NullStringMap INSTANCE=new NullStringMap();
	
	private NullStringMap() {
		
	}

	public void clear() {
		
	}

	public boolean containsKey(Object key) {
		return false;
	}

	public boolean containsValue(Object value) {
		return false;
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return null;
	}

	public Object get(Object key) {
		return null;
	}

	public boolean isEmpty() {
		return true;
	}

	public Set<String> keySet() {
		return new NullSet<String>();
	}

	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return 0;
	}

	public Collection<Object> values() {
		return new NullSet<Object>();
	}
}
