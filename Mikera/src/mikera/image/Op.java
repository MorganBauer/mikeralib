package mikera.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

public class Op {

	public static BufferedImage add(BufferedImage ba, double fa, BufferedImage bb, double fb) {
		int h=ba.getHeight();
		int w=ba.getWidth();
		BufferedImage result=Generator.newImage(w,h);
		for (int y=0; y<h; y++) {
			for (int x=0; x<w; x++) {
				int ca=ba.getRGB(x, y);
				int cb=bb.getRGB(x, y);
				double r=(Colours.getRed(ca)*fa+Colours.getRed(cb)*fb);
				double g=(Colours.getGreen(ca)*fa+Colours.getGreen(cb)*fb);
				double b=(Colours.getBlue(ca)*fa+Colours.getBlue(cb)*fb);
				double a=(Colours.getAlpha(ca)*fa+Colours.getAlpha(cb)*fb);
				
				result.setRGB(x, y, Colours.getARGBClamped(r,g,b,a));
			}
		}
		return result;
	}

	public static BufferedImage apply(BufferedImage b, BufferedImageOp op) {
		return op.filter(b, null);
	}

	public static BufferedImage apply(BufferedImage b, ImageFilter filter) {
		return Op.createImage(b.getWidth(), b.getHeight(),new FilteredImageSource(b.getSource(),filter));
	}

	public static BufferedImage merge(BufferedImage b1, BufferedImage b2, double proportion2) {
		BufferedImage result=Generator.newImage(b1.getWidth(),b1.getHeight());
		Graphics2D g=result.createGraphics();
		g.drawImage(b1,0,0,null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)proportion2));
		g.drawImage(b2,0,0,null);
		return result;
	}

	public static BufferedImage resize(BufferedImage source, int w, int h) {
		BufferedImage b=Generator.newImage(w,h);
		Graphics2D g=b.createGraphics();
		// g.setTransform(AffineTransform.getScaleInstance(w/(double)source.getWidth(), h/(double)source.getHeight()));
		g.drawImage(source, 0, 0, w, h, null);
		return b;
	}

	public static BufferedImage createImage(int w, int h, ImageProducer ip) {
		Image img=Toolkit.getDefaultToolkit().createImage(ip);
		BufferedImage result=Generator.newImage(w,h);
		Graphics2D gr=result.createGraphics();
		//gr.setComposite(AlphaComposite.Src);
		gr.drawImage(img, 0,0,w,h, null);
		return result;
	}

}
