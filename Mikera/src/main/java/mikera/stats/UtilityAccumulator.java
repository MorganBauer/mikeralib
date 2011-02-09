package mikera.stats;

public final class UtilityAccumulator implements Comparable<UtilityAccumulator>, Cloneable {
	int max_n=0;
	int n=0;
	double sum_u=0.0f;
	double sum_u2=0.0f;
	
	public UtilityAccumulator() {
		
	}
	
	public UtilityAccumulator(int maxCases) {
		max_n=maxCases;
	}
	
	public void recordUtility(double value) {
		sum_u+=value;
		sum_u2+=value*value;
		n++;
		
		if ((max_n>0)&&(n>max_n)) {
			double f=((double)max_n)/n;
			sum_u*=f;
			sum_u2*=f;
			n=max_n;
		}
	}

	public int n() {
		return n;
	}
	
	public double mean() {
		double mean=sum_u/n;
		
		return mean;
	}
	
	public double var() {
		double value=(n*sum_u2-sum_u*sum_u)/(n*(n-1));
		return value;
	}
	
	public double sd() {
		return Math.sqrt(var());
	}

	public int compareTo(UtilityAccumulator b) {
		UtilityAccumulator bcc=(UtilityAccumulator)b;
		
		double am=mean();
		double bm=bcc.mean();
		if (am<bm) return -1;
		if (am>bm) return 1;
		return 0;
	}
	
	public UtilityAccumulator clone() {
		UtilityAccumulator acc=null;
		try {
			acc=(UtilityAccumulator)super.clone();
		} catch (CloneNotSupportedException x) {}

		return acc;	
	}
	
	public void clear() {
		n=0;
		sum_u=0;
		sum_u2=0;
	}
}
