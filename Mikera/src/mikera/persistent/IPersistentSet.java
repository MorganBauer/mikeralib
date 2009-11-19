package mikera.persistent;

import java.util.Collection;
import java.util.Set;

public interface IPersistentSet<T> extends Set<T> {
	
	public PersistentSet<T> delete(final T value);
	
	public PersistentSet<T> deleteAll(final Collection<T> values);

	public PersistentSet<T> deleteAll(final PersistentCollection<T> values);

	public PersistentSet<T> includeAll(final Collection<T> values);

	public PersistentSet<T> includeAll(final PersistentSet<T> values);

	public boolean allowsNulls();
}
