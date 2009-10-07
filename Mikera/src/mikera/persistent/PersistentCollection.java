package mikera.persistent;

import java.io.Serializable;
import java.util.*;

public interface PersistentCollection<T> extends Collection<T>, Cloneable, Serializable {

	public PersistentCollection<T> deleteAll(T value);

	public PersistentCollection<T> deleteAll(Collection<T> values);

	public int size();
}
