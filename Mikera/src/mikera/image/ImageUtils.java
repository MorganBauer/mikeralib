package mikera.image;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.*;

import mikera.util.Rand;


public class ImageUtils {
	public static BufferedImage getImage(String filename) {
		URL imageURL = ImageUtils.class.getResource(filename);
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
		Frame f=new Frame("Image popup");
		Canvas c=new Canvas() {
			public void paint(Graphics g) {
				g.drawImage(image,0,0,null);
			}
		};
		c.setMinimumSize(new Dimension(image.getWidth(null),image.getHeight(null)));
		f.add(c);
		f.setVisible(true);
		f.setMinimumSize(new Dimension(image.getWidth(null)+10,image.getHeight(null)+30));
		
		return f;
	}
	
	@SuppressWarnings("serial")
	public static Frame displayAndExit(Image image) {
		final Frame f=display(image);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.dispose();
			}
		});
		return f;
	}

}
