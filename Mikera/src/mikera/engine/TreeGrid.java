package mikera.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import mikera.util.Bits;
import mikera.util.Maths;
import mikera.util.Tools;

/**
 * Grid implemented as a hierarchy of 4*4*4 grids
 * 
 * Top level is offset to centre at (0,0,0)
 * 
 * Very fast!!
 * 
 * @author Mike Anderson
 *
 * @param <T>
 */
public class TreeGrid<T> extends BaseGrid<T> {

	private static final int DIM_SPLIT_BITS=2;
	//private static final int DIM_SPLIT_SIZE=1<<DIM_SPLIT_BITS;
	private static final int SIGNIFICANT_BITS=20;
	private static final int TOP_SHIFT=SIGNIFICANT_BITS-DIM_SPLIT_BITS;
	private static final int DATA_ARRAY_SIZE=1<<(3*DIM_SPLIT_BITS);
	private static final int SIGNIFICANT_MASK=(1<<SIGNIFICANT_BITS)-1;
	private static final int TOP_OFFSET=(1<<(SIGNIFICANT_BITS-1));
	private static final int TOP_MAX=SIGNIFICANT_MASK;
	
	// each cell contains either object of type T or a sub-grid
	private final Object[] data=new Object[DATA_ARRAY_SIZE];
	
	public int countNonNull() {
		return countNonNull(TOP_SHIFT);
	}
	
	@SuppressWarnings("unchecked")
	public int countNodes() {
		int res=0;
		for (int i=0; i<DATA_ARRAY_SIZE; i++) {
			Object d=data[i];
			if (d==null) continue;
			if (d instanceof TreeGrid<?>) {
				TreeGrid<T> tg=(TreeGrid<T>)d;
				res+=tg.countNodes();
			}
		}
		return res+1;
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
	
	public T get(int x, int y, int z) {
		return getLocal(x+TOP_OFFSET,y+TOP_OFFSET,z+TOP_OFFSET);
	}
	
	@SuppressWarnings("unchecked")
	public T getLocal(int x, int y, int z) {
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
	
	public void visitBlocks(BlockVisitor<T> bf) {
		visitBlocksLocal(bf,
				0,0,0,
				0,0,0,
				TOP_MAX,TOP_MAX,TOP_MAX,
				TOP_SHIFT);
	}
	
	public void visitBlocks(BlockVisitor<T> bf,int x1, int y1, int z1,int x2, int y2, int z2) {
		visitBlocksLocal(bf,
				0,0,0,
				x1+TOP_OFFSET,y1+TOP_OFFSET,z1+TOP_OFFSET,
				x2+TOP_OFFSET,y2+TOP_OFFSET,z2+TOP_OFFSET,
				TOP_SHIFT);
	}
	
	@SuppressWarnings("unchecked")
	// cx,cy,cz are offset to bottom left of grid
	// x1,y1,z1,x2,y2,z2 relative to bottom left
	private void visitBlocksLocal(BlockVisitor<T> bf, int cx, int cy, int cz,int x1, int y1, int z1,int x2, int y2, int z2,int shift) {
		int li=0;
		int bsize=1<<shift; // size of sub blocks in this TreeGrid
		
		int max= (bsize<<2); // top limit of this whole TreeGrid
		
		for (int lz=0; lz<max; lz+=bsize) {
			if ((lz>z2)||((lz+bsize)<=z1)) {
				li+=16;
				continue;
			}
			for (int ly=0; ly<max; ly+=bsize) {
				if ((ly>y2)||((ly+bsize)<=y1)) {
					li+=4;
					continue;
				}
				for (int lx=0; lx<max; lx+=bsize) {
					if ((lx>x2)||((lx+bsize)<=x1)) {
						li++;
						continue;
					}
					
					// start of inner loop
					Object d=data[li++];
					if (d==null) continue;
					if (d instanceof TreeGrid<?>) {
						TreeGrid<T> tg=(TreeGrid<T>)d;
						tg.visitBlocksLocal(
								bf, 
								cx+lx, 
								cy+ly, 
								cz+lz, 
								x1-lx,
								y1-ly,
								z1-lz,
								x2-lx,
								y2-ly,
								z2-lz,
								shift-DIM_SPLIT_BITS);
					} else {
						int p1=cx+Math.max(lx,x1);
						int p2=cy+Math.max(ly,y1);
						int p3=cz+Math.max(lz,z1);
						int q1=cx+Math.min(lx+bsize-1,x2);
						int q2=cy+Math.min(ly+bsize-1,y2);
						int q3=cz+Math.min(lz+bsize-1,z2);
						bf.visit(
								p1-TOP_OFFSET,
								p2-TOP_OFFSET,
								p3-TOP_OFFSET,
								q1-TOP_OFFSET,
								q2-TOP_OFFSET,
								q3-TOP_OFFSET,
								(T)d);
					}
				}
			}
		}
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

	public void set(int x, int y, int z, T value) {
		setLocal(x+TOP_OFFSET,y+TOP_OFFSET,z+TOP_OFFSET,value);
	}
	
	@SuppressWarnings("unchecked")
	private void setLocal(int x, int y, int z, T value) {
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
		setBlock(x1+TOP_OFFSET, 
				y1+TOP_OFFSET, 
				z1+TOP_OFFSET, 
				x2+TOP_OFFSET, 
				y2+TOP_OFFSET, 
				z2+TOP_OFFSET, 
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
		int bx1=((x1)&(bmask));
		int by1=((y1)&(bmask));
		int bz1=((z1)&(bmask));
	
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

	public ArrayList<T> getObjectList(int x1, int y1, int z1, int x2, int y2, int z2) {
		final ArrayList<T> al=new ArrayList<T>();
		BlockVisitor<T> bv=new BlockVisitor<T>() {

			@Override
			public Object visit(int x1, int y1, int z1, int x2, int y2, int z2,
					T value) {
				al.add(value);
				return null;
			}
			
		};
		visitBlocks(bv,x1,y1,z1,x2,y2,z2);
		return al;
	}
	

}
