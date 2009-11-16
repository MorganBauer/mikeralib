package mikera.persistent.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import mikera.persistent.*;
import mikera.util.Tools;
import mikera.util.emptyobjects.NullList;

/**
 * Singleton set instance
 * 
 * @author Mike Anderson
 *
 * @param <T>
 */
public final class SingletonSet<T> extends BasePersistentSet<T> {
	final T value;
	
	@SuppressWarnings("unchecked")
	public static <T> SingletonSet create(T object) {
		return new SingletonSet<T>(object);
	}
	
	public int size() {
		return 1;
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	private SingletonSet(T object) {
		value=object;
	}

	@Override
	public PersistentSet<T> include(T value) {
		return SetFactory.concat(this,value);
	}
	
	public boolean contains(Object o) {
		return Tools.equalsWithNulls(value, o);
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int pos=0;
			public boolean hasNext() {
				return (pos<1);
			}

			public T next() {
				if (pos>0) throw new NoSuchElementException();
				pos++;
				return value;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
