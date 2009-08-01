package mikera.util;

/**
 * Helpful maths functions
 * 
 * Focus on using floats rather than doubles for speed
 * 
 * @author Mike
 *
 */
public final class Maths {
	public static final float ROOT_TWO=(float)Math.sqrt(2);
	public static final float ROOT_THREE=(float)Math.sqrt(3);
	public static final float PI=(float)Math.PI;
	
	public static float sqrt(float a) {
		return (float)Math.sqrt(a);
	}
	
	public static int clampToInteger(double value, int min, int max) {
		int v=(int)value;
		if (v<min) return min;
		if (v>max) return max;
		return v;
	}
	
	public static int clampToInteger(float value, int min, int max) {
		int v=(int)value;
		if (v<min) return min;
		if (v>max) return max;
		return v;
	}
	
	public static int clamp(int value, int min, int max) {
		if (value<min) return min;
		if (value>max) return max;
		return value;
	}
	
	public static double clamp(double value, double min, double max) {
		if (value<min) return min;
		if (value>max) return max;
		return value;
	}
	
	public static int sign(double a) {
		if (a==0.0f) return 0;
		return (a>0)?1:-1;
	}
	
	public static int sign(float a) {
		if (a==0.0f) return 0;
		return (a>0)?1:-1;
	}
	
	public static int sign(int a) {
		if (a==0) return 0;
		return (a>0)?1:-1;
	}
	
	public static int sign(long a) {
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
	
	public static float sin(float a) {
		return (float)Math.sin(a);
	}
	
	public static float cos(float a) {
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
	
	public static float round(float f, int dp) {
		float factor=(float)Math.pow(10, -dp);
		return Math.round(f/factor)*factor;
	}
}
