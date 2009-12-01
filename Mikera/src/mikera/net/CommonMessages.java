package mikera.net;

import java.nio.*;

public class CommonMessages {
	public static final byte JOIN_GAME=0;
	
	public static void addJoinMessage(ByteBuffer dest, String name, String pass) {
		dest.put(CommonMessages.JOIN_GAME);
		Util.writeASCIIString(dest, name);
		Util.writeASCIIString(dest, pass);
	}
	
	public static String getString(ByteBuffer bb) {
		return Util.readASCIIString(bb);
	}
	
	public static void addConfirmJoinMessage(Data dest) {
		dest.appendByte(CommonMessages.JOIN_GAME);

	}
	
}
