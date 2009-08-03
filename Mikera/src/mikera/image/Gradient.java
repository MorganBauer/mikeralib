package mikera.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;

import mikera.util.Maths;

public class Gradient {
	public static final int SIZE=256;
	
	public static int[] create() {
		return new int[SIZE];
	}
	
	public static int[] createInvertedGradient() {
		int[] gr=create();
		for (int i=0; i<SIZE; i++) {
			int c=(255-i)*0x010101;
			gr[i]=c|Colours.ALPHA_MASK;
		}
		
		return gr;
	}
	
	public static void fillFromImage(int[] grad, BufferedImage b, int line) {
		for (int i=0; i<SIZE; i++) {
			grad[i]=b.getRGB(i, line);
		}
	}

	
	public static int[] createRainbowGradient() {
		int[] gr=create();
		int size=SIZE;
		for (int i=0; i<size; i++) {
			float h=((float)i)/size;
			gr[i]=Color.HSBtoRGB(h, 1, 1)|Colours.ALPHA_MASK;
		}
		
		return gr;
	}
	
	public void fillLinearGradient(int[] grad, int p1, int c1, int p2, int c2) {
		grad[p2]=c2;
		int d=Maths.sign(p2-p1);
		if (d==0) return;
		
		final float[] fs=new float[12];
		Colours.toFloat4(fs, 0, c1); // c1
		Colours.toFloat4(fs, 4, c2); // c2
		Colours.toFloat4(fs, 8, c1); // current
		float df=1.0f/(p2-p1);
		for (int i=p1; i!=p2; i+=d) {
			grad[i]=Colours.fromFloat4(fs, 8);
			fs[8] +=df*(fs[4]-fs[0]);
			fs[9] +=df*(fs[5]-fs[1]);
			fs[10]+=df*(fs[6]-fs[2]);
			fs[11]+=df*(fs[7]-fs[3]);
		}
	}
	
	static class RGBComponentGradientFilter extends RGBImageFilter {
		public int[] gradient;
	
		public RGBComponentGradientFilter(int[] g) {
			gradient=g;
		}
		
		@Override
		public int filterRGB(int x, int y, int argb) {
	    	int r=Colours.getRed(gradient[Colours.getRed(argb)]);
	    	int g=Colours.getGreen(gradient[Colours.getGreen(argb)]);
	    	int b=Colours.getBlue(gradient[Colours.getBlue(argb)]);
	    	int a=Colours.getAlpha(argb);
	
	    	return Colours.getARGBQuick(r, g, b,a);
	    }
	}
	
	static class IntensityRGBGradientFilter extends RGBImageFilter {
		public int[] gradient;
	
		public IntensityRGBGradientFilter(int[] g) {
			gradient=g;
		}
		
		@Override
		public int filterRGB(int x, int y, int argb) {
	    	int lum=Colours.getLuminance(argb);
	    	int rgb=gradient[lum]&Colours.RGB_MASK;
	    	return Colours.getARGB(rgb, Colours.getAlpha(argb));
	    }
	}
	
	public static BufferedImage applyToRGBComponents(BufferedImage b, int[] g) {
		ImageFilter filter=new RGBComponentGradientFilter(g);
		BufferedImage result=Op.apply(b, filter);
		return result;
	}
	
	public static BufferedImage applyToIntensity(BufferedImage b, int[] g) {
		ImageFilter filter=new IntensityRGBGradientFilter(g);
		BufferedImage result=Op.apply(b, filter);
		return result;
	}
}
