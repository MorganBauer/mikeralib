package mikera.util;

/**
 * Helpful maths functions
 * 
 * @author Mike
 *
 */
public final class Maths {
	public static final float ROOT_TWO=(float)Math.sqrt(2);
	public static final float ROOT_THREE=(float)Math.sqrt(3);
	
	public static float sqrt(float a) {
		return (float)Math.sqrt(a);
	}
	
	public static int clamp(int a, int min, int max) {
		if (a<min) return min;
		if (a>max) return max;
		return a;
	}
	
	public static int sign(float a) {
		if (a==0.0f) return 0;
		return (a>0)?1:-1;
	}
	
	public static int sign(int a) {
		if (a==0) return 0;
		return (a>0)?1:-1;
	}
	
	public static float min(float a, float b, float c) {
		float result=a;
		if (b<result) result=b;
		if (c<result) result=c;
		return result;
	}
	
	public static float max(float a, float b, float c) {
		float result=a;
		if (b>result) result=b;
		if (c>result) result=c;
		return result;
	}
	
	public static int abs(int a) {
		if (a<0) return -a;
		return a;
	}
	
	public static int min(int a, int b, int c) {
		int result=a;
		if (b<result) result=b;
		if (c<result) result=c;
		return result;
	}
	
	public static int max(int a, int b, int c) {
		int result=a;
		if (b>result) result=b;
		if (c>result) result=c;
		return result;
	}
	
	public static float sin(double a) {
		return (float)Math.sin(a);
	}
	
	public static float cos(double a) {
		return (float)Math.cos(a);
	}
	
	public static float floor(float a) {
		return (float)Math.floor(a);
	}

	public static int square(byte b) {
		return b*b;
	}
	
	public static float square(float a) {
		return a*a;
	}
	

}
