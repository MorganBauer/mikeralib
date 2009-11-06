package mikera.engine;

public interface Grid<T> {
	public void set(int x, int y, int z, T value);
	
	public T get(int x, int y, int z);
	
	public void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value);

	public int countNodes();
	
	public int countNonNull();
	
	public void visitBlocks(BlockVisitor<T> bf);
	
	public void clear();
	
	public void paste(Grid<T> t);
	
	public void paste(Grid<T> t, final int dx, final int dy, final int dz);
	
	public void set(Grid<T> o);
}
