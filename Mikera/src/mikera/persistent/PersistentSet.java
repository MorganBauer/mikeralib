package mikera.persistent;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import mikera.persistent.impl.FilteredIterator;
import mikera.persistent.impl.SingletonList;
import mikera.util.Tools;

public abstract class PersistentSet<T> extends PersistentCollection<T> implements IPersistentSet<T> {
	private static final long serialVersionUID = -6984657587635163165L;

	@Override
	public abstract PersistentSet<T> include(final T value);
	
	@Override
	public PersistentSet<T> include(final Collection<T> values) {
		PersistentSet<T> ps=this;
		for (T t: values) {
			ps=ps.include(t);
		}
		return ps;
	}
	
	/**
	 * Default implementation for include
	 * Note: should be overridden if faster implementation is possible
	 * @param values
	 * @return
	 */
	public PersistentSet<T> include(final PersistentSet<T> values) {
		return include((Collection<T>)values);
	}

	public PersistentSet<T> clone() {
		return (PersistentSet<T>)super.clone();
	}
	
	public PersistentSet<T> deleteAll(final T value) {
		if (!contains(value)) return this;
		Iterator<T> it=new FilteredIterator<T>(iterator()) {
			@Override
			public boolean filter(Object testvalue) {
				return (!Tools.equalsWithNulls(value, testvalue));
			}		
		};
		return SetFactory.create(it);
	}

	public PersistentSet<T> deleteAll(final Collection<T> values) {
		Iterator<T> it=new FilteredIterator<T>(iterator()) {
			PersistentCollection<T> col=ListFactory.create(values);
			
			@Override
			public boolean filter(Object value) {
				return (!col.contains(value));
			}		
		};
		return SetFactory.create(it);
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if ((o instanceof PersistentSet<?>)) return equals((PersistentSet<T>)o);
		return false;
	}
	
	public boolean equals(PersistentSet<T> s) {
		if (size()!=s.size()) return false;
		return s.containsAll(this)&&(this.containsAll(s));
	}
}
