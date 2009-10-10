package mikera.net;

import java.nio.ByteBuffer;


import mikera.net.*;

public class Server {
	public static final int SERVER_PORT=8080;
	public static final int SERVER_TICK_MILLIS=150;
	
	private ServerConnector serverConnector;
	
	private Thread serverThread;
	boolean running=false;
	double gameTime=0.0;
	private int load_avg=0;
	
	private PlayerList playerList=new PlayerList();
	
	/*
	 * ===============================================================
	 * Server startup and shutdown
	 * ===============================================================
	 */
	
	public void start() {
		stop();
		serverConnector=new ServerConnector();
		serverConnector.setMessageHandler(new Receiver());
		serverConnector.startListening(SERVER_PORT);
		
		initServer();

		System.err.println("Server started on port: "+SERVER_PORT);
	}
	
	
	public void stop() {
		if (serverConnector==null) return;
		
		running=false;
		serverConnector.close();
		serverConnector=null;
		System.err.println("Server stopped");
		
	}
	
	private void initServer() {
		gameTime=0.0;
		running=true;
		Thread theThread=new Thread(new Runnable() {
			
			public void run() {
				while (running) {
					try {
						long lastMillis=System.currentTimeMillis();

						// server tick
						doTick();
						
						// calculate load
						long timeNow=System.currentTimeMillis();					
						int load=(int)((100*(timeNow-lastMillis))/SERVER_TICK_MILLIS);
						load_avg=(load_avg*9+load)/10;
						//System.err.println("Server tick load = "+load+"%");
						lastMillis=timeNow;
						
						// sleep
						int sleepTime=SERVER_TICK_MILLIS;
						Thread.sleep(sleepTime);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		serverThread=theThread;
		serverThread.start();
	}

	
	/*
	 * ===============================================================
	 * Server logging
	 * ===============================================================
	 */
	
	private void logMessage(String s) {
		System.err.println(s);
	}

	/*
	 * ===============================================================
	 * Messaging
	 * ===============================================================
	 */
	
	public void sendMessage(Data data, int playerNo) {
		Connection c=playerList.getPlayer(playerNo).connection;
		c.write(data);
	}
	
	
	/*
	 * ===============================================================
	 * Server tick handling
	 * ===============================================================
	 */

	private void doTick() {
		processQueuedMessages();
	}
	
	private void processQueuedMessages() {
		int max=playerList.listSize();
		for (int i=0; i<max; i++) {
			Player p=playerList.getPlayer(i);
			processQueuedMessages(p);
		}
	}
	
	private void processQueuedMessages(Player p) {
	
	}
	
	/*
	 * ===============================================================
	 * Connection message handling
	 * ===============================================================
	 */
	private class Receiver implements MessageHandler {
		public boolean handleMessage(ByteBuffer data, Connection c) {
			if (c.userTag==null) {
				handleConnectRequest(data,c);
			} else {
				queueMessage(data,(Integer)c.userTag);
			}
			return false; // since we want to keep the ByteBuffer data.....
		}
	}
	
	private void handleConnectRequest(ByteBuffer data, Connection c) {
		// TODO: security! validate name / pass
		byte m=data.get();
		if (m!=CommonMessages.JOIN_GAME) {
			c.close();
			throw new Error("First message must be JOIN_GAME was "+m+" with length "+(data.remaining()+1));
		}
		String name=CommonMessages.getString(data);
		String pass=CommonMessages.getString(data);
		
		Integer playerID=playerList.addPlayer(name, pass);
		Player p=playerList.getPlayer(playerID);
		p.connection=c;
		
		logMessage("Player connected: ID="+playerID+" name='"+name+"'");
	}

	// queues message for the relevant player
	private void queueMessage(ByteBuffer data, Integer playerID) {
		Player p=playerList.getPlayer(playerID);
		if (p==null) {
			System.err.println("Message received from non-existent player?!?!? ID="+playerID);
			return;
		}
		p.messages.add(data);
	}
}
