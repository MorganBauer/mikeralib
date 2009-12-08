package mikera.engine;

import mikera.util.Maths;

public abstract class BaseGrid<T> extends Grid<T> implements Cloneable {
	
	public void set(Grid<T> o) {
		clear();
		paste(o);
	}
	
	public void paste(Grid<T> t) {
		paste(t,0,0,0);
	}
	
	public int countNodes() {
		return 1;
	}
	
	public void visitPoints(final PointVisitor<T> bf) {
		BlockVisitor<T> bv=new BlockVisitor<T>() {
			public Object visit(int x1, int y1, int z1, int x2, int y2, int z2,
					T value) {
				for (int z=z1; z<=z2; z++) {
					for (int y=y1; y<=y2; y++) {
						for (int x=x1; x<=x2; x++) {
							bf.visit(x, y, z, value);
						}
					}
				}
				return null;
			}		
		};
		visitBlocks(bv);
	}
	
	public void visitBlocks(final BlockVisitor<T> bf, final int xmin, final int ymin, final int zmin, final int xmax, final int ymax, final int zmax) {
		BlockVisitor<T> bv=new BlockVisitor<T>() {
			public Object visit(int x1, int y1, int z1, int x2, int y2, int z2,
					T value) {
				x1=Maths.max(x1,xmin);
				x2=Maths.min(x2,xmax);
				y1=Maths.max(y1,ymin);
				y2=Maths.min(y2,ymax);
				z1=Maths.max(z1,zmin);
				z2=Maths.min(z2,zmax);
				if ((x1<=x2)&&(y1<=y2)&&(z1<=z2)) {
					bf.visit(x1, y1, z1, x2,y2,z2, value);
				}
				return null;
			}		
		};
		visitBlocks(bv);
	}

	
	public void visitPoints(final PointVisitor<T> bf, final int xmin, final int xmax, final int ymin, final int ymax, final int zmin, final int zmax) {
		BlockVisitor<T> bv=new BlockVisitor<T>() {
			public Object visit(int x1, int y1, int z1, int x2, int y2, int z2,
					T value) {
				x1=Maths.max(x1,xmin);
				x2=Maths.min(x2,xmax);
				y1=Maths.max(y1,xmin);
				y2=Maths.min(y2,xmax);
				z1=Maths.max(z1,xmin);
				z2=Maths.min(z2,xmax);
				
				if ((z2<z1)||(y2<y1)||(x2<x1)) return null;
				
				for (int z=z1; z<=z2; z++) {
					for (int y=y1; y<=y2; y++) {
						for (int x=x1; x<=x2; x++) {
							bf.visit(x, y, z, value);
						}
					}
				}
				return null;
			}		
		};
		visitBlocks(bv);
	}


	
	public void changeAll(final T value) {
		BlockVisitor<T> changer=new BlockVisitor<T>() {
			public Object visit(int x1, int y1, int z1, int x2, int y2, int z2,
					T v) {
				if (v!=null) {
					setBlock(x1,y1,z1,
							x2, y2, z2, value);
				}
				return null;
			}
		};
		visitBlocks(changer);
	}

	public void paste(Grid<T> t, final int dx, final int dy, final int dz) {
		BlockVisitor<T> paster=new BlockVisitor<T>() {
			public Object visit(int x1, int y1, int z1, int x2, int y2, int z2,
					T value) {
				setBlock(x1+dx,y1+dy,z1+dz,
						x2+dx, y2+dy, z2+dz, value);
				return null;
			}
		};
		t.visitBlocks(paster);
	}

	public void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value) {
		for (int z=z1; z<=z2; z++) {
			for (int y=y1; y<=y2; y++) {
				for (int x=x1; x<=x2; x++) {
					set(x,y,z,value);
				}	
			}		
		}
	}
}