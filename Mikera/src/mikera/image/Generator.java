package mikera.image;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.*;
import mikera.engine.*;

import mikera.util.Maths;
import mikera.util.Rand;

public class Generator {
	
	public static BufferedImage merge(BufferedImage b1, BufferedImage b2, double proportion2) {
		TransparencyFilter tf=new TransparencyFilter();
		tf.factor=(int)Maths.clampToInteger(255*proportion2,0,255);
		BufferedImage result=new BufferedImage(b1.getWidth(),b1.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=result.createGraphics();
		g.drawImage(b1,0,0,null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)proportion2));
		g.drawImage(b2,0,0,null);
		return result;
	}
	
	static class TransparencyFilter extends RGBImageFilter {
	    int factor=255;
		public int filterRGB(int x, int y, int rgb) {
			int alpha=(rgb&0xff000000)>>24;
			int newAlpha=(alpha*factor)>>8;
	    	return (rgb & 0x00ffffff)
				| ((newAlpha&255)<<24);
	    }
	}
	
	static class GreyFilter extends RGBImageFilter {
		public int filterRGB(int x, int y, int rgb) {
	    	int lum=(77*((rgb>>16)&255)+150*((rgb>>8)&255)+29*((rgb)&255))>>8;
	    	return (rgb&0xFF000000)|(0x010101*lum);
	    }
	}
	
	static class MultiplyFilter extends RGBImageFilter {
		public float r_factor=1.0f;
		public float g_factor=1.0f;
		public float b_factor=1.0f;
		public float a_factor=1.0f;
	
		public MultiplyFilter() {}
		
		public MultiplyFilter(double r, double g, double b) {
			r_factor=(float) r;
			g_factor=(float) g;
			b_factor=(float) b;
		}
		
		public MultiplyFilter(double r, double g, double b, double a) {
			this(r,g,b);
			a_factor=(float)a;
		}
		
		public int filterRGB(int x, int y, int rgb) {
	    	int r=((rgb>>16)&255);
	    	int g=((rgb>>8)&255);
	    	int b=((rgb)&255);
	    	int a=((rgb>>24)&255);
	    	r=Maths.clampToInteger(r*r_factor, 0, 255);
	    	g=Maths.clampToInteger(g*g_factor, 0, 255);
	    	b=Maths.clampToInteger(b*b_factor, 0, 255);
	    	a=Maths.clampToInteger(a*a_factor, 0, 255);

	    	return ((a<<24)|(r<<16)|(g<<8)|b);
	    }
	}
	
	public static BufferedImage blurImage(BufferedImage bi) {
		Kernel kernel = new Kernel(3, 3,
		        new float[] {
		            1/9f, 1/9f, 1/9f,
		            1/9f, 1/9f, 1/9f,
		            1/9f, 1/9f, 1/9f});
		BufferedImageOp op = new ConvolveOp(kernel);
		return op.filter(bi, null);
	}
	
	public static BufferedImage createSolidImage(int w, int h,int c) {
		BufferedImage result=newImage(w,h);
		Graphics2D g=result.createGraphics();
		g.setColor(new Color(c));
		g.fillRect(0, 0, w, h);
		return result;
	}
	
	public static BufferedImage newImage(int w, int h) {
		BufferedImage result=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		return result;
	}
	
	private static PerlinNoise perlin=new PerlinNoise();
	
	public static BufferedImage createPerlinNoise(int w, int h, double s) {
		BufferedImage b=newImage(w,h);
		float sx=(float)s/w;
		float sy=(float)s/h;
		
		for (int y=0; y<h; y++) {
			for (int x=0; x<w; x++) {
				double nv=perlin.noise3(x*sx, y*sy,0);
				int a=Maths.clamp((int)(128+128*1.9*nv), 0, 255);
				b.setRGB(x, y, 0xFF000000+0x010101*a);
			}
		}
		return b;
	}
	
	public static BufferedImage createWhiteNoise(int w, int h) {
		BufferedImage b=newImage(w,h);
		
		for (int y=0; y<h; y++) {
			for (int x=0; x<w; x++) {
				int a=Rand.r(256);
				b.setRGB(x, y, 0xFF000000+0x010101*a);
			}
		}
		return b;
	}

	/**
	 * Test function
	 */
	public static void main(String[] args) {
		BufferedImage b=createPerlinNoise(256,256,20);
		BufferedImage c=createWhiteNoise(256,256);
		BufferedImage d=merge(b,c,0.5);	
		BufferedImage e=blurImage(d);	
		
		ImageUtils.displayAndExit(e);
	}
}
