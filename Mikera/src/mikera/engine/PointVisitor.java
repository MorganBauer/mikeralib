/**
 * 
 */
package mikera.engine;

public abstract class PointVisitor<T> {
	public abstract Object visit(int x, int y, int z, T value);
}