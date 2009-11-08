package mikera.math;

public abstract class BaseVector implements IVector {

	public BaseVector clone() {
		try {
			return (BaseVector)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
		
	}
	
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append('(');
		int s=size();
		for (int i=0; i<s; i++) {
			sb.append(get(i));
			if ((i+1)<s) sb.append(", ");
		}		
		sb.append(')');
		return sb.toString();
	}
}
