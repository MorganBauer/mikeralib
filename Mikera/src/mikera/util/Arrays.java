package mikera.util;

import mikera.util.emptyobjects.NullArrays;

public class Arrays {
	public static final float[] NULL_FLOATS=NullArrays.NULL_FLOATS;
	public static final int[] NULL_INTS=NullArrays.NULL_INTS;
	public static final byte[] NULL_BYTES=NullArrays.NULL_BYTES;
	public static final double[] NULL_DOUBLES=NullArrays.NULL_DOUBLES;
	
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
	
	public static float squareDistance(float[] a, float[] b) {
		float eSquared=0;
		for (int i=0; (i<a.length); i++) {
			float d=a[i]-b[i];
			eSquared+=d*d;
		}
		return eSquared;
	}
	
	public static void fillRandom(float[] a) {
		fillRandom(a,0,a.length);
	}
	
	public static void fillRandom(float[] a, int start, int length) {
		for (int i=0; i<length; i++) {
			a[start+i]=Rand.nextFloat();
		}
	}
}
