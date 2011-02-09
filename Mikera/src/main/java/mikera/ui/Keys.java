package mikera.ui;

import java.util.HashMap;

public class Keys {
	 
	private final HashMap<Integer,Boolean> keys=new HashMap<Integer,Boolean>();
	
	public void setKey(int i, boolean b) {
		keys.put(i, b);
	}
	
	public boolean getKey(int i) {
		Boolean b=keys.get(i);
		return (b!=null)&&(b.booleanValue());
	}
}
