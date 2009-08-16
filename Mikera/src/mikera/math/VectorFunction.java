package mikera.math;

public interface VectorFunction extends Function<Vector,Vector> {
	public int inputDimensions();
	
	public int outputDimensions();
}
