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
	public static final float E=(float)Math.exp(1);
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
	
	public static int middle(int a, int b, int c) {
		if (a<b) {
			if (b<c) {
				return b;
			}
			return (a<c)?c:a;
		} else {
			if (a<c) {
				return a;
			}
			return (b<c)?c:b;
		}
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
	
	public static float fmod(float n, float d) {
		float x=n/d;
		return n-floor(x)*d;
	}
	
	public static int mod(int n, int d) {
		int r= (n%d);
		if (r<0) r+=d;
		return r;
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
	
	public static int min(int a, int b) {
		return (a<b)?a:b;
	}
	
	public static int max(int a, int b) {
		return (a>b)?a:b;
	}
	
	public static float min(float a, float b) {
		return (a<b)?a:b;
	}
	
	public static float max(float a, float b) {
		return (a>b)?a:b;
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
	
	public static int floor(float a) {
		if (a>=0) return (int)a;
		int x=(int)a;
		return (a==x)?x:x-1;
	}
	
	public static int floor(double a) {
		if (a>=0) return (int)a;
		int x=(int)a;
		return (a==x)?x:x-1;
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

	/**
	 * Computes a fast approximation to <code>Math.pow(a, b)</code>. Adapted
	 * from <url>http://www.dctsystems.co.uk/Software/power.html</url>.
	 * 
	 * @param a a positive number
	 * @param b a number
	 * @return a^b
	 */
	public static final float fastPower(float a, float b) {
	    // adapted from: http://www.dctsystems.co.uk/Software/power.html
	    float x = Float.floatToRawIntBits(a);
	    x *= 1.0f / (1 << 23);
	    x = x - 127;
	    float y = x - (int) Math.floor(x);
	    b *= x + (y - y * y) * 0.346607f;
	    y = b - (int) Math.floor(b);
	    y = (y - y * y) * 0.33971f;
	    return Float.intBitsToFloat((int) ((b + 127 - y) * (1 << 23)));
	}

	public static final float clamp(float x, float min, float max) {
	    if (x > max)
	        return max;
	    if (x > min)
	        return x;
	    return min;
	}

	public static final double min(double a, double b, double c) {
	    if (a > b)
	        a = b;
	    if (a > c)
	        a = c;
	    return a;
	}

	public static final float min(float a, float b, float c, float d) {
	    if (a > b)
	        a = b;
	    if (a > c)
	        a = c;
	    if (a > d)
	        a = d;
	    return a;
	}

	public static final double max(double a, double b, double c) {
	    if (a < b)
	        a = b;
	    if (a < c)
	        a = c;
	    return a;
	}

	public static final float max(float a, float b, float c, float d) {
	    if (a < b)
	        a = b;
	    if (a < c)
	        a = c;
	    if (a < d)
	        a = d;
	    return a;
	}

	public static final float smoothStep(float a, float b, float x) {
	    if (x <= a)
	        return 0;
	    if (x >= b)
	        return 1;
	    float t = clamp((x - a) / (b - a), 0.0f, 1.0f);
	    return t * t * (3 - 2 * t);
	}
	
	public static final float lerp(float t,float a, float b) {
	    return (1-t) * a + t*b;		
	}
	
	public static final float smoothFactor(float t) {
	    return t * t * (3 - 2 * t);		
	}
}
