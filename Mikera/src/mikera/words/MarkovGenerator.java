package mikera.words;

import mikera.util.*;
import java.util.*;

public class MarkovGenerator {
	public HashMap<String,ProbabilityPicker<Character>> chances=new HashMap<String,ProbabilityPicker<Character>>();
	
	private int chainLength;
	
	public MarkovGenerator(int cl) {
		chainLength=cl;
	}
	
	public void load(String s) {
		// TODO
		for (int i=0; i<s.length(); i++) {
			String ss=s.substring(Maths.max(0,i-chainLength),i);
			Character c=s.charAt(i);
		}
	}
}
