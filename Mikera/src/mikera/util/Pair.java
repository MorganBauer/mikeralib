package mikera.util;

import java.io.Serializable;

/**
 * Immutable pair class
 * @author Mike
 *
 * @param <A>
 * @param <B>
 */
public class Pair<A,B> implements Cloneable, Comparable<Pair<A,B>>, Serializable {
	private static final long serialVersionUID = -7930545169533958038L;
	public final A a;
	public final B b;
	
	public Pair() {
		a=null;
		b=null;
	}
	
	public Pair(A a, B b) {
		this.a=a;
		this.b=b;
	}
	
	public boolean equals(Pair<A,B> p) {
		if (p==null) return false;
		
		if (a==null) {
			if (p.a!=null) return false;
		} else {
			if (!a.equals(p.a)) return false;
		}
		
		if (b==null) {
			if (p.b!=null) return false;
		} else {
			if (!b.equals(p.b)) return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public int compareTo(Pair<A,B> p) {
		if (a instanceof Comparable) {
			if (a!=null) {
				Comparable c=(Comparable)a;
				int v=c.compareTo((Comparable)p.a);
				if (v!=0) return v;
			}	
		} else {
			throw new Error("Can't compare Pair: first component type not comparable");
		}
		
		if (b instanceof Comparable) {
			if (b!=null) {
				Comparable c=(Comparable)b;
				int v=c.compareTo((Comparable)p.b);
				if (v!=0) return v;
			}	
		}
		return 0;
	}
	
	public Pair<B,A> swap() {
		return new Pair<B,A>(b,a);
	}
	
	public Pair<A,B> clone() {
		return this;
	}
	
	public int hashCode() {
		int result=0;
		if (a!=null) result+=a.hashCode();
		if (b!=null) result+=Bits.rollLeft(b.hashCode(),16);
		return result;
	}
}
