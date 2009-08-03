package mikera.image;

import java.awt.Color;

import mikera.util.Maths;

public class Colours {
	public static int RGB_MASK=0xFFFFFF;
	public static int ALPHA_MASK=0xFF000000;
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
    	int ri=Maths.clampToInteger(r*MAX_BYTE, 0, 255);
    	int gi=Maths.clampToInteger(g*MAX_BYTE, 0, 255);
    	int bi=Maths.clampToInteger(b*MAX_BYTE, 0, 255);
    	int ai=Maths.clampToInteger(a*MAX_BYTE, 0, 255);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static int getARGBClamped(float r, float g, float b, float a) {
    	int ri=Maths.clampToInteger(r*MAX_BYTE, 0, 255);
    	int gi=Maths.clampToInteger(g*MAX_BYTE, 0, 255);
    	int bi=Maths.clampToInteger(b*MAX_BYTE, 0, 255);
    	int ai=Maths.clampToInteger(a*MAX_BYTE, 0, 255);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static int getARGBClamped(int r, int g, int b, int a) {
    	int ri=Maths.clamp(r, 0, 255);
    	int gi=Maths.clamp(g, 0, 255);
    	int bi=Maths.clamp(b, 0, 255);
    	int ai=Maths.clamp(a, 0, 255);
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
	
	public Color getRGBColor(int argb) {
		int r=getRed(argb);
		int g=getGreen(argb);
		int b=getBlue(argb);
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
