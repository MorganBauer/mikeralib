package mikera.image;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.*;


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
}
