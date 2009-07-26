package mikera.util;

public final class Rand {
		
	// For gaussians
	private static final java.util.Random rand=getGenerator();
	

	private static long state=System.nanoTime()|1;

	/**
	 * Gets a long random value
	 * uses very fast "XORShift" algorithm
	 * @return
	 */
	public static long nextLong() {
		long a=state;
		a ^= (a << 21);
		a ^= (a >>> 35);
		a ^= (a << 4);
		state=a;
		return a;
	}
		
	private static class MikeraRandom extends java.util.Random {
		long state=System.nanoTime()|1;
		
		protected int next(int bits) {
			return (int)(nextLong()>>>(64-bits));
		}
		
		public long nextLong() {
			state ^= (state << 21);
			state ^= (state >>> 35);
			state ^= (state << 4);
			return state;
		}
		
		public void setSeed(long seed) {
			if (seed==0) seed=54384849948L;
			state=seed;
		}
	}
	
	public static java.util.Random getGenerator() {
		return new MikeraRandom();
	}
	
	// Poisson distribution
	public static int po(double x) {
		int r = 0;
		double a = nextDouble();
		if (a >= 0.99999999)
			return 0;
		double p = Math.exp(-x);
		while (a >= p) {
			r++;
			a = a - p;
			p = p * x / r;
		}
		return r;
	}

	public static int po(int numerator, int denominator) {
		return po(((double) numerator) / denominator);
	}

	
	public static final int nextInt() {
		return (int)(nextLong()>>32);
	}
	

	
	/**
	 * Random number from zero to s-1
	 * 
	 * @param s Upper bound (excluded)
	 * @return
	 */
	public static final int r(int s) {
		if (s<=0) return 0;
		long result=((nextLong()>>>32)*s)>>32;
		return (int) result;
	}

	
	private static final double DOUBLE_SCALE_FACTOR=1.0/Math.pow(2,63);

	/**
	 * Returns standard double in range 0..1
	 * 
	 * @return
	 */
	public static final double nextDouble() {
		return (nextLong()>>>1)*DOUBLE_SCALE_FACTOR;
	}
	
	public static final float nextFloat() {
		return (float)nextDouble();
	}

    /**
     *  Returns random number uniformly distributed in inclusive [n1, n2] range.
     *  It is allowed to have to n1 > n2, or n1 < n2, or n1 == n2.
     */
     public static final int range(int n1, int n2) {
        if (n1>n2) {
        	int t=n1; n1=n2; n2=t;
        }
        return n1+r(n2-n1+1);
     }
	
	// simulates a dice roll with the given number of sides
	public static final int d(int sides) {
		return r(sides) + 1;
	}
	
	public static final int d3() {
		return d(3);
	}

	public static final int d4() {
		return d(4);
	}

	public static final int d6() {
		return d(6);
	}

	public static final int d8() {
		return d(8);
	}

	public static final int d10() {
		return d(10);
	}

	public static final int d12() {
		return d(12);
	}

	public static final int d20() {
		return d(20);
	}

	public static final int d100() {
		return d(100);
	}
	
	public static int d(int number, int sides) {
		int total = 0;
		for (int i = 0; i < number; i++) {
			total += d(sides);
		}
		return total;
	}
	
	public static double n(double u, double sd) {
		return rand.nextGaussian()*sd+u;
	}
}
