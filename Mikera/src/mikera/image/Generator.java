package mikera.image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.*;
import mikera.engine.*;

import mikera.util.Maths;
import mikera.util.Rand;

public class Generator {
	
	public static BufferedImage blurImage(BufferedImage bi) {
		BufferedImageOp op = ImageFilters.blurOperation;
		return op.filter(bi, null);
	}
	
	public static BufferedImage tileImage(BufferedImage bi, int tw, int th) {
		int h=bi.getHeight();
		int w=bi.getWidth();
		BufferedImage result=newImage(w*tw,h*th);
		Graphics2D gr=result.createGraphics();
		for (int y=0; y<th; y++) {
			for (int x=0; x<tw; x++) {
				gr.drawImage(bi,x*w,y*h,w,h,null);
			}
		}
		return result;
	}
	
	public static BufferedImage unTileImage(BufferedImage bi, int tw, int th) {
		int h=bi.getHeight()/tw;
		int w=bi.getWidth()/th;
		BufferedImage result=newImage(w,h);
		Graphics2D gr=result.createGraphics();
		gr.drawImage(bi,-w/2,-h/2,null);
		return result;
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
				//double nv=perlin.noise3(x*sx, y*sy,0);
				double nv=perlin.tileableNoise3(x*sx, y*sy, 0 , w*sx, h*sy ,256);
				int a=Maths.clampToInteger((128+128*1.9*nv), 0, 255);
				b.setRGB(x, y, Colours.ALPHA_MASK+0x010101*a);
			}
		}
		return b;
	}
	
	public static BufferedImage createChecker(int w, int h, int tiles, int c1, int c2) {
		BufferedImage b=newImage(w,h);
		
		for (int y=0; y<h; y++) {
			for (int x=0; x<w; x++) {
				int i=x*tiles/w+y*tiles/h;
				b.setRGB(x, y, ((i&1)==0)?c1:c2);
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
		BufferedImage o=createChecker(256,256,16,0xFFFFFFFF,0xFFC080C0);
		// BufferedImage o=createSolidImage(256,256,0xFFFFFFFF);
		
		BufferedImage b=createPerlinNoise(1024,1024,20);
		BufferedImage c=createWhiteNoise(1024,1024);
		BufferedImage e=Op.merge(b,c,0.1);	
		
		e=Op.resize(e,256,256);

		
		Graphics2D gr=o.createGraphics();
		gr.drawImage(e, 0, 0, null);
		o=tileImage(o,3,3);
		ImageUtils.displayAndExit(o);
	}
}
