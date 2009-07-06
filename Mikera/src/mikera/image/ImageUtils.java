package mikera.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;


public class ImageUtils {
	public static Image getImage(String filename) {
		URL imageURL = ImageUtils.class.getResource(filename);
		if (imageURL != null) {
			return getImage(imageURL);
		}
		throw new Error("Image URL not found");
	}

	public static Image getImage(URL imageURL) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		Image image = null;
		image = toolkit.getImage(imageURL);

		return image;
	}
}
