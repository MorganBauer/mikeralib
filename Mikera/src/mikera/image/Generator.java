package mikera.image;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.*;
import mikera.engine.*;

import mikera.util.Maths;

public class Generator {
	public static BufferedImage merge(BufferedImage b1, BufferedImage b2, double proportion2) {
		TransparencyFilter tf=new TransparencyFilter();
		tf.factor=(int)Maths.clamp(255*proportion2,0,255);
		BufferedImage result=new BufferedImage(b1.getWidth(),b1.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=result.createGraphics();
		g.drawImage(b1,0,0,null);
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

	/**
	 * Test function
	 */
	public static void main(String[] args) {
		BufferedImage b=createPerlinNoise(256,256,20);
			
		ImageUtils.displayAndExit(b);
	}
}
