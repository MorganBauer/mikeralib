package mikera.math;

public abstract class VectorFunction implements Function<Vector,Vector> {
	public static final int ARBITRARY_DIMENSIONS=-1;
	
	public int inputDimensions() {
		return ARBITRARY_DIMENSIONS;
	}
	
	public int outputDimensions() {
		return ARBITRARY_DIMENSIONS;
	}
}
