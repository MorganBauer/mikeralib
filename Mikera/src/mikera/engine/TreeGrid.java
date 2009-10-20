package mikera.engine;

import java.util.Arrays;

/**
 * Grid implemented as a heirarchy of 4*4*4 grids
 * 
 * @author Mike Anderson
 *
 * @param <T>
 */
public class TreeGrid<T> extends BaseGrid<T> {

	
	private static final int DIM_SPLIT_BITS=2;
	private static final int SIGNIFICANT_BITS=20;
	private static final int TOP_SHIFT=SIGNIFICANT_BITS-DIM_SPLIT_BITS;
	private static final int DATA_ARRAY_SIZE=1<<(3*DIM_SPLIT_BITS);

	// each cell contains either object of type T or a sub-grid
	private final Object[] data=new Object[DATA_ARRAY_SIZE];
	
	@SuppressWarnings("unchecked")
	public T get(int x, int y, int z) {
		int shift=TOP_SHIFT;
		TreeGrid<T> head=this;
		while (shift>=0) {
			int lx=(x>>shift)&3;
			int ly=(y>>shift)&3;
			int lz=(z>>shift)&3;
			int li=lx+(ly<<2)+(lz<<4);
			Object d=head.data[li];
			if (d==null) return null;
			if (!(d instanceof TreeGrid<?>)) {
				return (T)d;
			}
			shift-=2;
			head=(TreeGrid<T>)d;
		}
		throw new Error("This shouldn't happen!!");
	}
	
	public void clear() {
		Arrays.fill(data, null);
	}
	
	public TreeGrid() {
		
	}
	
	public TreeGrid(T defaultvalue) {
		for (int i=0; i<data.length; i++) {
			data[i]=defaultvalue;
		}
	}

	@SuppressWarnings("unchecked")
	public void set(int x, int y, int z, T value) {
		int shift=TOP_SHIFT;
		TreeGrid<T> head=this;
		while (shift>=0) {
			int lx=(x>>shift)&3;
			int ly=(y>>shift)&3;
			int lz=(z>>shift)&3;
			int li=lx+(ly<<2)+(lz<<4);
			Object d=head.data[li];
			if ((d==null)&&(shift>0)) {
				if (value==null) return;
				d=new TreeGrid<T>();
				head.data[li]=d;
			}
			if (shift==0) {
				head.data[li]=value;
				if (head.isSolid(value)) solidify(x,y,z,TOP_SHIFT);
				return;
			} else if (!(d instanceof TreeGrid<?>)) {
				if (d.equals(value)) return;
				d=new TreeGrid<T>((T)d);
				head.data[li]=d;				
			}
			shift-=2;
			head=(TreeGrid<T>)d;
		}
		throw new Error("This shouldn't happen!!");
	}
	
	private boolean isSolid(T value) {
		for (int i=0; i<DATA_ARRAY_SIZE; i++) {
			Object d=data[i];
			if (!((d==value)||((d!=null)&&(d.equals(value))))) {
				return false;
			}
		}
		return true;
	}
	
	private void solidify(int x, int y, int z, int shift) {
		int lx=(x>>shift)&3;
		int ly=(y>>shift)&3;
		int lz=(z>>shift)&3;
		int li=lx+(ly<<2)+(lz<<4);
		Object d=data[li];
		// TODO
		if (shift>0) solidify(x,y,z,shift-1);
		
	}

}
