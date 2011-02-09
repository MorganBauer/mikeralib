package mikera.engine;

import mikera.annotations.Mutable;

@Mutable
public abstract class Grid<T> {
	// core get/set functions
	
	public abstract void set(int x, int y, int z, T value);
	
	public abstract T get(int x, int y, int z);
	
	// query functions
	
	public abstract int countNodes();
	
	public abstract int countNonNull();
	
	// visitor functions
	
	public abstract void visitPoints(PointVisitor<T> bf);

	public abstract void visitBlocks(BlockVisitor<T> bf);

	public abstract void visitPoints(PointVisitor<T> bf, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax);

	public abstract void visitBlocks(BlockVisitor<T> bf, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax);

	// bulk change operations
	
	public abstract void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value);

	public abstract void clear();

	public abstract void clearContents();

	public abstract void paste(Grid<T> t);
	
	public abstract void paste(Grid<T> t, final int dx, final int dy, final int dz);
	
	public abstract void set(Grid<T> o);
	
	
	
	public static boolean pointWithin(int x, int y, int z, int x1, int y1, int z1, int x2, int y2, int z2) {
		return ((x>=x1)&&(x<=x2)&&(y>=y1)&&(y<=y2)&&(z>=z1)&&(z<=z2));
	}
	
	public static boolean areaIntersect(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int x4, int y4, int z4) {
		return ((x4>=x1)&&(x3<=x2)&&(y4>=y1)&&(y3<=y2)&&(z4>=z1)&&(z3<=z2));
	}
	
	public void validate() {
		
	}
}
