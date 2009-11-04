package mikera.engine;

import java.util.Arrays;

import mikera.util.Maths;
import mikera.util.Tools;

/**
 * Grid implemented as a hierarchy of 4*4*4 grids
 * 
 * Very fast!!
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
	private static final int SIGNIFICANT_MASK=(1<<SIGNIFICANT_BITS)-1;
	
	// each cell contains either object of type T or a sub-grid
	private final Object[] data=new Object[DATA_ARRAY_SIZE];
	
	public int countNonNull() {
		return countNonNull(TOP_SHIFT);
	}
	
	@SuppressWarnings("unchecked")
	private int countNonNull(int shift) {
		int res=0;
		for (int i=0; i<DATA_ARRAY_SIZE; i++) {
			Object d=data[i];
			if (d==null) continue;
			if (d instanceof TreeGrid<?>) {
				if (shift<=0) throw new Error("TreeGrid element where shift="+shift);
				TreeGrid<T> tg=(TreeGrid<T>)d;
				res+=tg.countNonNull(shift-DIM_SPLIT_BITS);
			} else {
				res+=1<<(3*shift);
			}
		}
		return res;
	}
	
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
			if (!(Tools.equalsWithNulls(value,d))) {
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean isSolid() {
		Object d=data[0];
		return (!(d instanceof TreeGrid<?>))&&isSolid((T)data[0]);
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
		setBlock(x1, 
				y1, 
				z1, 
				x2, 
				y2, 
				z2, 
				value,
				TOP_SHIFT);
	}

	@SuppressWarnings("unchecked")
	protected Object setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value, int shift) {
		int bmask=3<<shift;
		int bstep=1<<shift;
		boolean setData=false;
		
		// get coordinates of sub block containing point 1
		// note masking to keep correct sign
		int bx1=((x1)&(bmask))|(x1&(~SIGNIFICANT_MASK));
		int by1=((y1)&(bmask))|(y1&(~SIGNIFICANT_MASK));
		int bz1=((z1)&(bmask))|(z1&(~SIGNIFICANT_MASK));
	
		// loop over sub blocks (lx,ly,lz)-(ux,uy,uz)
		for (int lz=bz1; lz<=z2; lz+=bstep) {
			for (int ly=by1; ly<=y2; ly+=bstep) {
				for (int lx=bx1; lx<=x2; lx+=bstep) {
					int li=index(lx,ly,lz,shift);
					Object d=data[li];
					if (Tools.equalsWithNulls(d, value)) continue;
					
					int ux=lx+bstep-1;
					int uy=ly+bstep-1;
					int uz=lz+bstep-1;
					if ((shift<=0)||((z1<=lz)&&(z2>=uz)&&(y1<=ly)&&(y2>=uy)&&(x1<=lx)&&(x2>=ux))) {
						// set entire sub block
						data[li]=value;
						setData=true;
					} else {
						if (d==null) {
							d=new TreeGrid<T>();
							data[li]=d;
						} else if (!(d instanceof TreeGrid<?>)) {
							d=new TreeGrid<T>((T)d);
							data[li]=d;
						}
						TreeGrid<T> tg=(TreeGrid<T>)d;
						Object nd=tg.setBlock(
								Maths.max(lx, x1)-lx,
								Maths.max(ly, y1)-ly,
								Maths.max(lz, z1)-lz,
								Maths.min(x2, ux)-lx,
								Maths.min(y2, uy)-ly,
								Maths.min(z2, uz)-lz,
								value,
								shift-DIM_SPLIT_BITS);
						if (nd!=d) {
							setData=true;
							data[li]=nd;
						}
					}
				}
			}
		}
		if (setData&&isSolid()) return data[0];
		return this;
	}
	
	/*
	 * Old version of setBlock
	private void setBlockLooped(int x1, int y1, int z1, int x2, int y2, int z2, T value) {

		for (int z=z1; z<=z2; z++) {
			for (int y=y1; y<=y2; y++) {
				for (int x=x1; x<=x2; x++) {
					set(x,y,z,value);
				}	
			}		
		}
	}
	*/

}
