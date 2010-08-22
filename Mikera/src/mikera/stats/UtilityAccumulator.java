package mikera.stats;

public final class UtilityAccumulator implements Comparable<UtilityAccumulator>, Cloneable {
	int n=0;
	double sum_u=0.0f;
	double sum_u2=0.0f;
		
	public void recordUtility(float u) {
		sum_u+=u;
		sum_u2+=u*u;
		n++;
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
