package mikera.persistent;

import java.io.Serializable;
import java.util.*;

public interface PersistentCollection<T> extends Collection<T>, Cloneable, Serializable {

	public PersistentCollection<T> deleteAll(T value);

	public PersistentCollection<T> deleteAll(PersistentCollection<T> values);

	public int size();
}
