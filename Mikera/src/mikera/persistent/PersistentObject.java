package mikera.persistent;
import java.io.Serializable;

/**
 * Base for all mikera.persistent classes
 * 
 * @author Mike Anderson
 *
 */
public abstract class PersistentObject implements IPersistentObject {
	private static final long serialVersionUID = -4077880416849448410L;

	/**
	 * Clone returns the same object since PersistentObject
	 * and all subclasses must be immutable
	 * 
	 */
	public PersistentObject clone() {
		return this;
	}
}
