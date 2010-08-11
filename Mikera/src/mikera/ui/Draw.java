package mikera.ui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;

import mikera.util.Maths;

public class Draw {


	public static void drawImageWithBorder(Graphics g,int x , int y, int w, int h,BufferedImage image, int bs, boolean drawCentre) {
		int ih=image.getHeight();
		int iw=image.getWidth();
		int scw=iw-2*bs;
		int sch=ih-2*bs;
		
		int dx0=x;
		int dx1=x+bs;
		int dx2=x+w-bs;
		int dx3=x+w;
		
		int dy0=y;
		int dy1=y+bs;
		int dy2=y+h-bs;
		int dy3=y+h;
		
		int sx0=0;
		int sx1=bs;
		int sx2=iw-bs;
		int sx3=iw;
		
		int sy0=0;
		int sy1=bs;
		int sy2=ih-bs;
		int sy3=ih;
		
		g.drawImage(image, dx0, dy0, dx1, dy1, sx0, sy0, sx1, sy1, null);
		for (int xx=dx1; xx<dx2; xx+=scw) {
			int dw=Maths.min(scw,dx2-xx);
			g.drawImage(image, xx, dy0, xx+dw, dy1, sx1, sy0, sx1+dw, sy1, null);
		}
		g.drawImage(image, dx2, dy0, dx3, dy1, sx2, sy0, sx3, sy1, null);
		
		for (int yy=dy1; yy<dy2; yy+=sch) {
			int dh=Maths.min(sch,dy2-yy);
			g.drawImage(image, dx0, yy, dx1, yy+dh, sx0, sy1, sx1, sy1+dh, null);
			if (drawCentre) for (int xx=dx1; xx<dx2; xx+=scw) {
				int dw=Maths.min(scw,dx2-xx);
				g.drawImage(image, xx, yy, xx+dw, yy+dh, sx1, sy1, sx1+dw, sy1+dh, null);
			}
			g.drawImage(image, dx2, yy, dx3, yy+dh, sx2, sy1, sx3, sy1+dh, null);
		}
		
		g.drawImage(image, dx0, dy2, dx1, dy3, sx0, sy2, sx1, sy3, null);
		for (int xx=dx1; xx<dx2; xx+=scw) {
			int dw=Maths.min(scw,dx2-xx);
			g.drawImage(image, xx, dy2, xx+dw, dy3, sx1, sy2, sx1+dw, sy3, null);
		}
		g.drawImage(image, dx2, dy2, dx3, dy3, sx2, sy2, sx3, sy3, null);
	}
	

	public static void drawImageTiled(Graphics g,int x , int y, int w, int h,BufferedImage image) {
		int ih=image.getHeight();
		int iw=image.getWidth();

		for (int iy=y; iy<(y+h); iy+=ih) {
			for (int ix=x; ix<(x+w); ix+=iw) {
				int dw=Maths.min(iw,(x+w)-ix);
				int dh=Maths.min(ih,(y+h)-iy);
				g.drawImage(image, ix, iy, ix+dw, iy+dh, 0, 0, dw, dh, null);
			}
		}
	}

	public static void drawCentredText(Graphics g, String s, int x, int y) {
		FontMetrics fm=g.getFontMetrics();
		int width=fm.stringWidth(s);
		int ascent=fm.getAscent();
		int descent=fm.getDescent();
		int height=ascent+descent;
		
		
		g.drawString(s,x-width/2,y-height/2+ascent);
	}
	
	public static void drawOutlineRect(Graphics g, int x, int y, int w, int h) {
		g.drawLine(x, y, x+w-1, y);
		g.drawLine(x, y+h-1, x+w-1, y+h-1);
		
		g.drawLine(x, y, x, y+h-1);
		g.drawLine(x+w-1, y, x+w-1, y+h-1);
	}
	
}
