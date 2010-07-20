package mikera.util;

import java.io.InputStream;
import java.net.URL;

public class Resource {
	public static URL getResource(String filename) {
		return Thread.currentThread().getContextClassLoader().getResource(filename);
	}
	
	public static InputStream getResourceAsStream(String filename) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
	}
}
