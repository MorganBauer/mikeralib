package mikera.persistent;

import java.io.Serializable;
import java.util.Collection;

public interface IPersistentCollection<T> extends Collection<T>, Cloneable, Serializable {

	public PersistentCollection<T> deleteAll(final T value);
	
	public PersistentCollection<T> deleteAll(final Collection<T> values);
}
