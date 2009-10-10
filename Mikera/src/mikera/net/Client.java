package mikera.net;

import mikera.net.*;
import java.nio.*;

public class Client {
	private ClientConnector clientConnector=new ClientConnector();
	private Connection connection=null;
	
	public void connectLocal() {
		connect("127.0.0.1", Server.SERVER_PORT);
	}
	
	public void connect(String address, int port) {
		closeConnection();
		connection=clientConnector.connect(address, port);
		
	}
	
	private void closeConnection() {
		if (connection!=null) {
			connection.close();
			connection=null;
		}
	}

	public void login(String name, String pass) {
		if (connection==null) throw new Error("Connection not established!");
		ByteBuffer bb=BufferCache.indirectInstance().getBuffer(1000);
		CommonMessages.addJoinMessage(bb, name, pass);
		bb.flip();
		connection.write(bb);
	}
}
