package mikera.persistent;
import java.io.Serializable;

public abstract class PersistentObject implements IPersistentObject {
	private static final long serialVersionUID = -4077880416849448410L;

	public PersistentObject clone() {
		try {
			return (PersistentObject)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
