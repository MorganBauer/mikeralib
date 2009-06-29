package mikera.engine;

public interface BlockVisitor<T> {
	public Object visit(int x1, int y1, int z1,int x2, int y2, int z2, T value);
}
