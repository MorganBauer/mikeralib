package mikera.util;

public class Arrays {
	public static final float[] NULL_FLOATS=new float[0];
	
	public static final int[] NULL_INTS=new int[0];
	
	public static final byte[] NULL_BYTES=new byte[0];
	
	public static final double[] NULL_DOUBLES=new double[0];
	
	public static <T extends Comparable<? super T>> boolean isSorted(T[] a, int start, int end) {
		while (start<end) {
			if (a[start].compareTo(a[start+1])>0) return false;
			start++;
		}
		return true;
	}
}
