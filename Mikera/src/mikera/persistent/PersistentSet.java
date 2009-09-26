package mikera.persistent;

import java.io.Serializable;
import java.util.Set;

public interface PersistentSet<T> extends Set<T>, PersistentCollection<T> {

}
