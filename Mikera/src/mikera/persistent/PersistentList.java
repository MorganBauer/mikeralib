package mikera.persistent;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface PersistentList<T> extends PersistentCollection<T>, List<T>, Comparable<PersistentList<T>> {
	public T get(int i);
	
	public int indexOf(Object value, int start);
	
	public PersistentList<T> append(T value);

	public PersistentList<T> append(PersistentList<T> values);

	public PersistentList<T> update(int index, T value);

	public PersistentList<T> insert(int index, T value);

	public PersistentList<T> insert(int index, Collection<T> values);

	public PersistentList<T> delete(int index);
	
	public PersistentList<T> delete(int start, int end);

	public PersistentList<T> deleteAll(Collection<T> values);
	
	public PersistentList<T> deleteFirst(T value);

	public <V> V[] toArray(V[] a);

	public <V> V[] toArray(V[] a, int offset);

	public PersistentList<T> subList(int fromIndex, int toIndex);
	
	/**
	 * Returns the front part of the list. Not guaranteed to be exactly half,
	 * but intended to be as balanced as possible
	 * @return
	 */
	public PersistentList<T> front();

	/**
	 * Returns the back part of the list. Not guaranteed to be exactly half,
	 * but intended to be as balanced as possible
	 * @return
	 */
	public PersistentList<T> back();

}
