package mikera.math;

import mikera.util.Maths;

public final class BigFloat extends Number {
	private static final long serialVersionUID = 8944436596909296283L;

	final double factor;
	final double exponent;
	
	public BigFloat(double d) {
		this(d,0);
	}
	
	public BigFloat(Number d) {
		this(d.doubleValue(),0);
	}
	
	private BigFloat(double d, double exp) {
		double af=Math.abs(d);
		if ((af!=0)&&((af<0.001)||(af>=10000))) {
			// normalise
			double e=Math.log(af);
			d=Maths.sign(d);
			exp+=e;
		}
		factor=d;
		exponent=exp;
	}
	
	public BigFloat multiply(BigFloat bf) {
		return new BigFloat(factor*bf.factor,exponent+bf.exponent);
	}
	
	public BigFloat multiply(double d) {
		return new BigFloat(factor*d,exponent);
	}
	
	public BigFloat divide(double d) {
		return new BigFloat(factor/d,exponent);
	}
	
	public BigFloat divide(BigFloat bf) {
		return new BigFloat(factor/bf.factor,exponent-bf.exponent);
	}
	
	public BigFloat add(double d) {
		return add(d,0);
	}
	
	public BigFloat add(BigFloat bf) {
		return add(bf.factor,bf.exponent);
	}
	
	public BigFloat subtract(double d) {
		return add(-d,0);
	}
	
	public BigFloat subtract(BigFloat bf) {
		return add(-bf.factor,bf.exponent);
	}
	
	private BigFloat add(double d, double e) {
		if (e<exponent) {
			d=d*(Math.exp(e-exponent))+factor;
			e=exponent;
		} else {
			d=d+factor*Math.exp(exponent-e);
		}
		return new BigFloat(d,e);
	}
	
	public static BigFloat exp(Number n) {
		return new BigFloat(1,n.doubleValue());
	}
	
	public double log() {
		return Math.log(factor)+exponent;
	}


	@Override
	public double doubleValue() {
		return factor*Math.exp(exponent);
	}

	@Override
	public float floatValue() {
		return (float)doubleValue();
	}

	@Override
	public int intValue() {
		return (int)doubleValue();
	}

	@Override
	public long longValue() {
		return (long)doubleValue();
	}
	
	public String toString() {
		return Double.toString(doubleValue());
	}
}
