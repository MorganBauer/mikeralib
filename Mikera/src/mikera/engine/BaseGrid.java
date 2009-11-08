package mikera.engine;

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
