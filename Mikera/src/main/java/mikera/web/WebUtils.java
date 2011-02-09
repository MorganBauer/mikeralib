package mikera.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebUtils {
	public static void launchBrowser(String s) {
		try {
			java.awt.Desktop.getDesktop().browse(new URI(s));
		} catch (Throwable e) {
			throw new Error(e);
		}
	}
}
