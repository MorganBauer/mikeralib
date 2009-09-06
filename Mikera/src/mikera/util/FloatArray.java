package mikera.util;

import mikera.util.emptyobjects.NullArrays;

public class FloatArray {
	public static final float[] NULL_FLOATS=NullArrays.NULL_FLOATS;
	
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
