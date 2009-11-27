package mikera.engine;

import mikera.annotations.Mutable;

@Mutable
public abstract class Grid<T> {
	public abstract void set(int x, int y, int z, T value);
	
	public abstract T get(int x, int y, int z);
	
	public abstract void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value);

	public abstract int countNodes();
	
	public abstract int countNonNull();
	
	public abstract void visitBlocks(BlockVisitor<T> bf);

	public abstract void visitPoints(PointVisitor<T> bf);

	public abstract void visitPoints(PointVisitor<T> bf, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax);

	public abstract void visitBlocks(BlockVisitor<T> bf, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax);

	
	public abstract void clear();
	
	public abstract void paste(Grid<T> t);
	
	public abstract void paste(Grid<T> t, final int dx, final int dy, final int dz);
	
	public abstract void set(Grid<T> o);
}
