package mikera.net;

import java.nio.ByteBuffer;

public interface MessageHandler {
	public void handleMessage(ByteBuffer data, Connection c);
}
