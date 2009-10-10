package mikera.net;

import java.nio.ByteBuffer;

public interface MessageHandler {
	/**
	 * Called when a full message contained in data is received
	 * 
	 * @param data the message data, excluding any header
	 * @param c the connection on which the message was received
	 */
	public boolean handleMessage(ByteBuffer data, Connection c);
}
