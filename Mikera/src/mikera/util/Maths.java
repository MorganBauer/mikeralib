package mikera.util;

public final class Maths {
	public static float sqrt(float a) {
		return (float)Math.sqrt(a);
	}
	
	public static int clamp(int a, int min, int max) {
		if (a<min) return min;
		if (a>max) return max;
		return a;
	}
}
