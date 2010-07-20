package mikera.image;

import mikera.math.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.*;
import javax.swing.JComponent;
import javax.swing.JFrame;

import mikera.util.Rand;
import mikera.util.Resource;


public class ImageUtils {

	public static BufferedImage getImage(String filename) {
		URL imageURL = Resource.getResource(filename);
		if (imageURL != null) {
			return getImage(imageURL);
		}
		throw new Error("Image URL not found");
	}

	public static BufferedImage getImage(URL imageURL) {
		BufferedImage image;
		try {
			image = ImageIO.read(imageURL);
		} catch (IOException e) {
			throw new Error("Image read failed", e);
		}

		return image;
	}
	
	public static int randColour() {
		return 0xFF000000+0x10000*Rand.r(256)+0x100*Rand.r(256)+Rand.r(256);
	}
	
	@SuppressWarnings("serial")
	public static Frame display(final Image image) {
		JFrame f=new JFrame("Image popup");
		JComponent c=new JComponent() {
			public void paint(Graphics g) {
				g.drawImage(image,0,0,null);
			}
		};
		c.setMinimumSize(new Dimension(image.getWidth(null),image.getHeight(null)));
		f.add(c);
		f.setVisible(true);
		f.setMinimumSize(new Dimension(image.getWidth(null),image.getHeight(null)));
		
		return f;
	}
	
	public static Frame displayAndExit(Image image) {
		final Frame frame=display(image);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		return frame;
	}
	
	public static Frame displayAndExit(Function<Vector,Vector> f) {
		BufferedImage image=Generator.createFunctionGradient(512,512,f, Gradient.createRainbowGradient(256));
		
		final Frame frame=display(image);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		return frame;
	}

}
