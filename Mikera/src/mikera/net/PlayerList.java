package mikera.net;

import java.util.*;
import java.nio.*;


public class PlayerList {
	public int MAX_PLAYERS=50;
	
	private ArrayList<Player> players=new ArrayList<Player>();
	private int playerCount=0;
	
	public Integer addPlayer(String name, String pass) {
		// find a free player id
		int playerNum=playerCount;
		for (int i=0; i<players.size(); i++) {
			Player p=players.get(i);
			if (p==null) {
				playerNum=i;
			} else {
				if (name.equals(p.name)) {
					throw new Error("Duplicate player name!!");
				}
			}
		}
		if (playerNum>=MAX_PLAYERS) throw new Error("Too many players!!");
		
		Player ps=new Player();
		ps.name=name;
		ps.password=pass;
		ps.id=Integer.valueOf(playerNum);
		
		if (playerNum>=players.size()) {
			players.add(ps);
		} else {
			players.set(playerNum, ps);			
		}
		playerCount++;
		
		return ps.id;
	}
	
	public int playerCount() {
		return playerCount;
	}
	
	public int listSize() {
		return players.size();
	}
	
	public void removePlayer(Integer id) {
		if (id==(players.size()-1)) {
			players.remove(id);
		} else {
			players.set(id,null);
		}
		playerCount--;
	}
	
	public Player getPlayer(int id) {
		return players.get(id);
	}
	
	public Integer findPlayer(String name) {
		for (int i=0; i<players.size(); i++) {
			Player ps=players.get(i);
			if ((ps!=null)&&name.equals(ps.name)) {
				return ps.id;
			}
		}
		return null;
	}
}
