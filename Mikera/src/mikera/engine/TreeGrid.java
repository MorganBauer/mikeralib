package mikera.engine;

import java.util.Arrays;

import mikera.util.Tools;

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
			int li=index(x,y,z,shift);
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
			int li=index(x,y,z,shift);
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
	
	private int index(int x, int y, int z, int shift) {
		int lx=(x>>shift)&3;
		int ly=(y>>shift)&3;
		int lz=(z>>shift)&3;
		int li=lx+(ly<<2)+(lz<<4);
		return li;
	}
	
	@SuppressWarnings("unchecked")
	private Object solidify(int x, int y, int z, int shift) {
		int li=index(x,y,z,shift);
		Object d=data[li];

		if (d instanceof TreeGrid<?>) {
			TreeGrid<T> g=(TreeGrid<T>)d;
			Object r=g.solidify(x,y,z,shift-DIM_SPLIT_BITS);
			if (r==g) return this;
			data[li]=r;
			d=r;
		} 
		
		if (isSolid((T)d)) {
			return d;
		}
		return this;
	}
	
	@Override
	public void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value) {
		setBlockLooped(x1, y1, z1, x2, y2, z2, value);
		
		// TODO: switch to smart version
		//setBlock(x1, y1, z1, x2, y2, z2, value,TOP_SHIFT);
	}

	protected void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value, int shift) {
		if (shift==0) {
			setBlockLooped(x1, y1, z1, x2, y2, z2, value);
			return;
		}
		int bmask=3<<shift;
		int bstep=1<<shift;
		int bx1=(x1)&(bmask);
		int by1=(y1)&(bmask);
		int bz1=(z1)&(bmask);
	
		// TODO: finish code for setting complete blocks
		for (int z=bz1; z<=z2; z+=bstep) {
			for (int y=by1; y<=y2; y+=bstep) {
				for (int x=bx1; x<=x2; x+=bstep) {
					int li=index(x,y,z,shift);
					Object d=data[li];
					if (Tools.equalsWithNulls(d, value)) continue;
					
					//int bx2=bx1+bstep-1;
					//int by2=by1+bstep-1;
					//int bz2=bz1+bstep-1;
					
					
					setBlockLooped(x1, y1, z1, x2, y2, z2, value);
				}
			}
		}
	}
	
	private void setBlockLooped(int x1, int y1, int z1, int x2, int y2, int z2, T value) {

		for (int z=z1; z<=z2; z++) {
			for (int y=y1; y<=y2; y++) {
				for (int x=x1; x<=x2; x++) {
					set(x,y,z,value);
				}	
			}		
		}
	}

}
