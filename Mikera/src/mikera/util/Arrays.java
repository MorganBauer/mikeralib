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
	
	public static void swap(int[] data, int a, int b) {
		int t=data[a];
		data[a]=data[b];
		data[b]=t;
	}
	
	public static int[] deduplicate(int[] data) {
		int di=0;
		int si=1;
		while (si<data.length) {
			int v=data[si];
			if (data[di]==v) {
				si++;
			} else {
				data[di+1]=v;
				di++;
				si++;
			}
		}
		di++;
		if (di<data.length) {
			int[] ndata=new int[di];
			System.arraycopy(data, 0, ndata, 0, di);
			return ndata;
		} else {
			return data;
		}
	}
}
