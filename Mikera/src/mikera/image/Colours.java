package mikera.image;

import java.awt.Color;

import mikera.math.Vector;
import mikera.util.Maths;

public class Colours {
	public static int RGB_MASK=   0x00FFFFFF;
	public static int RED_MASK=   0x00FF0000;
	public static int GREEN_MASK= 0x0000FF00;
	public static int BLUE_MASK=  0x000000FF;
	public static int ALPHA_MASK= 0xFF000000;
	public static final int BYTE_MASK=255;
	
	private static final int MAX_BYTE=255;
	private static final float INVERSE_FLOAT_FACTOR=1.0f/MAX_BYTE;
	
	public static int getRed(int argb) {
		return ((argb>>16)&255);
	}
	
	public static int getGreen(int argb) {
		return ((argb>>8)&255);
	}
	
	public static int getBlue(int argb) {
		return ((argb)&255);
	}
	
	public static int getAlpha(int argb) {
		return ((argb>>24)&255);
	}
	
	public static int toGreyScale(int argb) {
    	int lum=getLuminance(argb);
    	return (argb&ALPHA_MASK)|(0x010101*lum);
	}
	
	public static int getLuminance(int argb) {
    	int lum=(77*((argb>>16)&255)+150*((argb>>8)&255)+29*((argb)&255))>>8;
    	return lum;
	}
	
	public static int getARGBClamped(double r, double g, double b, double a) {
    	int ri=Maths.clampToInteger(r*MAX_BYTE, 0, MAX_BYTE);
    	int gi=Maths.clampToInteger(g*MAX_BYTE, 0, MAX_BYTE);
    	int bi=Maths.clampToInteger(b*MAX_BYTE, 0, MAX_BYTE);
    	int ai=Maths.clampToInteger(a*MAX_BYTE, 0, MAX_BYTE);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static int getARGBClamped(float r, float g, float b, float a) {
    	int ri=Maths.clampToInteger(r*MAX_BYTE, 0, MAX_BYTE);
    	int gi=Maths.clampToInteger(g*MAX_BYTE, 0, MAX_BYTE);
    	int bi=Maths.clampToInteger(b*MAX_BYTE, 0, MAX_BYTE);
    	int ai=Maths.clampToInteger(a*MAX_BYTE, 0, MAX_BYTE);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static int getARGBClamped3(Vector v) {
		int result=0xFF000000;
		float r=v.data[0];
    	result+=Maths.clampToInteger(r*MAX_BYTE, 0, MAX_BYTE)<<16;
		float g=v.data[1];
		result+=Maths.clampToInteger(g*MAX_BYTE, 0, MAX_BYTE)<<8;
		float b=v.data[2];
		result+=Maths.clampToInteger(b*MAX_BYTE, 0, MAX_BYTE);
		return result;
	}
	
	public static int getARGBClamped4(Vector v) {
		int result=0x00000000;
		float r=v.data[0];
    	result+=Maths.clampToInteger(r*MAX_BYTE, 0, MAX_BYTE)<<16;
		float g=v.data[1];
		result+=Maths.clampToInteger(g*MAX_BYTE, 0, MAX_BYTE)<<8;
		float b=v.data[2];
		result+=Maths.clampToInteger(b*MAX_BYTE, 0, MAX_BYTE);
		float a=v.data[3];
		result+=Maths.clampToInteger(a*MAX_BYTE, 0, MAX_BYTE)<<24;
		return result;
	}
	
	public static int getARGBClamped(int r, int g, int b, int a) {
    	int ri=Maths.clamp(r, 0, MAX_BYTE);
    	int gi=Maths.clamp(g, 0, MAX_BYTE);
    	int bi=Maths.clamp(b, 0, MAX_BYTE);
    	int ai=Maths.clamp(a, 0, MAX_BYTE);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static void toFloat4(float[] col, int offset, int argb) {
		col[offset]=getRed(argb)*INVERSE_FLOAT_FACTOR;
		col[offset+1]=getGreen(argb)*INVERSE_FLOAT_FACTOR;
		col[offset+2]=getBlue(argb)*INVERSE_FLOAT_FACTOR;
		col[offset+3]=getAlpha(argb)*INVERSE_FLOAT_FACTOR;
	}

	public static int fromFloat4(float[] col, int p) {
		return getARGBClamped(col[p],col[p+1],col[p+2],col[p+3]);
	}
	
	public static int fromVector(Vector col) {
		return fromFloat4(col.data,0);
	}
	
	public static int getARGB(int r, int g, int b, int a) {
		return getARGBQuick(r&255,g&255,b&255,a&255);
	}
	
	public static int getARGB(int r, int g, int b) {
		return getARGBQuick(r&255,g&255,b&255,255);
	}
	
	public static int getARGB(int rgb, int alpha) {
		return (rgb&RGB_MASK)|((alpha&BYTE_MASK)<<24);
	}
	
	static int getARGBQuick(int r, int g, int b, int a) {
		return (a<<24)|(r<<16)|(g<<8)|b;
	} 
	
	public Color getRGBColor(int rgb) {
		int r=getRed(rgb);
		int g=getGreen(rgb);
		int b=getBlue(rgb);
		return new Color(r,g,b);
	}
	
	public Color getColor(int argb) {
		int r=getRed(argb);
		int g=getGreen(argb);
		int b=getBlue(argb);
		int a=getAlpha(argb);
		return new Color(r,g,b,a);
	}
}
