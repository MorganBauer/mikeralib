package mikera.image;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

import mikera.util.Maths;

public class Gradient {
	public static final int SIZE=256;
	
	public int[] create() {
		return new int[SIZE];
	}
	
	public int[] createInvertedGradient() {
		int[] gr=create();
		for (int i=0; i<SIZE; i++) {
			int c=(255-i)*0x010101;
			gr[i]=c|Colours.ALPHA_MASK;
		}
		
		return gr;
	}
	
	
	static class RGBComponentGradientFilter extends RGBImageFilter {
		public int[] gradient;
	
		public RGBComponentGradientFilter(int[] g) {
			gradient=g;
		}
		
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
		
		public int filterRGB(int x, int y, int argb) {
	    	int lum=Colours.toGreyScale(argb);
	    	int c=gradient[lum];
	    	return Colours.getARGB(c, Colours.getAlpha(argb));
	    }
	}
	
	public BufferedImage applyToRGBComponents(BufferedImage b, int[] g) {
		BufferedImage result=Generator.newImage(b.getWidth(),b.getHeight());
		RGBComponentGradientFilter filter=new RGBComponentGradientFilter(g);
		Op.apply(b, filter);
		return result;
	}
}
