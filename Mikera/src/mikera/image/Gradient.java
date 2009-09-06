package mikera.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;

import mikera.math.Vector;
import mikera.math.VectorFunction;
import mikera.util.Maths;

public class Gradient {
	public static final int SIZE=256;
	
	/**
	 * Creates a new gradient array of default size (256)
	 * @return
	 */
	public static int[] create() {
		return new int[SIZE];
	}
	
	public static int[] createInvertedMonoGradient() {
		int[] gr=create();
		for (int i=0; i<SIZE; i++) {
			int c=(255-i)*0x010101;
			gr[i]=c|Colours.ALPHA_MASK;
		}	
		return gr;
	}
	
	/**
	 * Fills a gradient from a line in an image
	 * taking the first 256 pixels in the given line
	 * @param grad
	 * @param b
	 * @param line
	 */
	public static void fillFromImage(int[] grad, BufferedImage b, int line) {
		for (int i=0; i<SIZE; i++) {
			grad[i]=b.getRGB(i, line);
		}
	}
	
	public static void fillFromFunction(int[] grad, VectorFunction vf) {
		Vector col=new Vector(4);
		Vector pos=new Vector(1);
		col.data[3]=1.0f; // default to full alpha
		
		int s=grad.length;
		for (int i=0; i<s; i++) {
			col.data[0]=((float)i)/s;
			vf.calculate(pos, col);
			grad[i]=Colours.fromVector(col);
		}
	}

	/**
	 * Creates a rainbow coloured gradient
	 * @return
	 */
	public static int[] createRainbowGradient() {
		int[] gr=create();
		int size=SIZE;
		for (int i=0; i<size; i++) {
			float h=((float)i)/size;
			gr[i]=Color.HSBtoRGB(h, 1, 1)|Colours.ALPHA_MASK;
		}
		
		return gr;
	}
	
	/**
	 * Creates a gradient suitable for colouring a generated
	 * fractal landscape, with sea level at midpoint
	 * @return
	 */
	public static int[] createLandscapeGradient() {
		int[] gr=create();
		fillLinearGradient(gr, 0, 0xFF000000, 110, 0xFF0000FF);
		fillLinearGradient(gr, 111, 0xFF0000FF, 127, 0xFF0080FF);
		fillLinearGradient(gr, 128, 0xFFFFFF00, 130, 0xFFFFFF00);
		fillLinearGradient(gr, 131, 0xFF00FF00, 160, 0xFF006000);
		fillLinearGradient(gr, 161, 0xFF006000, 170, 0xFF808080);
		fillLinearGradient(gr, 171, 0xFF808080, 190, 0xFF707070);
		fillLinearGradient(gr, 191, 0xFFFFFFFF, 255, 0xFFB0FFFF);
		return gr;
	}
	
	/**
	 * Creates a monochrome gradient from black to white
	 * @return
	 */
	public static int[] createMonoGradient() {
		int[] gr=create();
		fillLinearGradient(gr, 0, 0xFF000000, 255, 0xFFFFFFFF);
		return gr;
	}
	
	/**
	 * Reverses a gradient
	 * @param grad
	 */
	public static void reverseGradient(int[] grad) {
		int s=grad.length;
		for (int i=0; i<s/2; i++) {
			int t=grad[i];
			grad[i]=grad[s-1-i];
			grad[s-1-i]=t;
		}
	}
	
	public static void fillLinearGradient(int[] grad, int p1, int c1, int p2, int c2) {
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
