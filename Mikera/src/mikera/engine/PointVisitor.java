/**
 * 
 */
package mikera.engine;

public abstract class PointVisitor<T> {
	public abstract boolean visit(int x, int y, int z, T value);
}