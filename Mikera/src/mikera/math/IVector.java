package mikera.math;

import java.io.Serializable;

public interface IVector extends Serializable, Cloneable {
	public int size();
	
	public float get(int i);
	
	public IVector clone();
}
