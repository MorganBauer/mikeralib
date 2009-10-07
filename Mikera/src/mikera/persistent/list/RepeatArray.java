package mikera.persistent.list;

import mikera.persistent.ListFactory;
import mikera.persistent.PersistentList;
import mikera.util.Tools;
import mikera.util.emptyobjects.NullList;

public class RepeatArray<T> extends BasePersistentArray<T> {
	private static final long serialVersionUID = -4991558599811750311L;

	final T value;
	final int count;
	
	private RepeatArray(T object, int num) {
		value=object;
		count=num;
	}
	
	public static <T> RepeatArray<T> create(T object, int number) {
		return new RepeatArray<T>(object,number);
	}
	
	public int size() {
		return count;
	}
	
	public T get(int i) {
		if ((i<0)||(i>=count)) throw new IndexOutOfBoundsException();
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public PersistentList<T> subList(int start, int end) {
		if ((start<0)||(end>count)) throw new IndexOutOfBoundsException();
		if (start>=end) {
			if (start==end) return (PersistentList<T>)NullList.INSTANCE;
			throw new IllegalArgumentException();
		}
		int num=end-start;
		if (num==count) return this;
		return create(value,num);
	}
	
	@SuppressWarnings("unchecked")
	public PersistentList<T> delete(int start, int end) {
		if ((start<0)||(end>count)) throw new IndexOutOfBoundsException();
		if (start>=end) {
			if (start==end) return this;
			throw new IllegalArgumentException();
		}
		int numDeleted=end-start;
		if (numDeleted==count) return (PersistentList<T>)NullList.INSTANCE;
		if (numDeleted==0) return this;
		return create(value,count-numDeleted);
	}
	
	public PersistentList<T> append(PersistentList<T> values) {
		if (values instanceof RepeatArray<?>) {
			RepeatArray<T> ra=(RepeatArray<T>)values;
			if (Tools.equalsWithNulls(ra.value, value)) {
				return create(value,ra.count+count);
			}
		}
		return super.append(values);
	}
}
