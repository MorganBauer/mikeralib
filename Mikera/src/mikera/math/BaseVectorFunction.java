package mikera.math;

public abstract class BaseVectorFunction implements VectorFunction {
	int inputDimensions=0;
	int outputDimensions=1;
	
	public int inputDimensions() {
		return inputDimensions;
	}

	public int outputDimensions() {
		return outputDimensions;
	}

}
