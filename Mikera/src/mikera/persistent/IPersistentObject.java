package mikera.persistent;

import java.io.Serializable;

public interface IPersistentObject extends Cloneable, Serializable {

	public IPersistentObject clone();
	
	public boolean hasFastHashCode();

}
