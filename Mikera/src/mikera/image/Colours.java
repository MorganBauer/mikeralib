package mikera.image;

import java.awt.Color;

import mikera.util.Maths;

public class Colours {
	public static int getRed(int argb) {
		return ((argb>>16)&255);
	}
	
	public static int getGreen(int argb) {
		return ((argb>>8)&255);
	}
	
	public static int getBlue(int argb) {
		return ((argb)&255);
	}
	
	public static int getAplha(int argb) {
		return ((argb>>24)&255);
	}
	
	public static int getARGB(double r, double g, double b, double a) {
    	int ri=Maths.clampToInteger(r, 0, 255);
    	int gi=Maths.clampToInteger(g, 0, 255);
    	int bi=Maths.clampToInteger(b, 0, 255);
    	int ai=Maths.clampToInteger(a, 0, 255);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static int getARGB(float r, float g, float b, float a) {
    	int ri=Maths.clampToInteger(r, 0, 255);
    	int gi=Maths.clampToInteger(g, 0, 255);
    	int bi=Maths.clampToInteger(b, 0, 255);
    	int ai=Maths.clampToInteger(a, 0, 255);
		return getARGBQuick(ri,gi,bi,ai);
	}
	
	public static int getARGB(int r, int g, int b, int a) {
		return getARGBQuick(r&255,g&255,b&255,a&255);
	}
	
	static int getARGBQuick(int r, int g, int b, int a) {
		return (a<<24)|(r<<16)|(g<<8)|b;
	} 
	
	public Color getColor(int argb) {
		int r=getRed(argb);
		int g=getGreen(argb);
		int b=getBlue(argb);
		return new Color(r,g,b);
	}
}
