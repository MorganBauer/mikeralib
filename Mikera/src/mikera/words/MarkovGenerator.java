package mikera.words;

import mikera.util.*;
import java.util.*;

public class MarkovGenerator {
	public HashMap<String,ProbabilityPicker<Character>> chances=new HashMap<String,ProbabilityPicker<Character>>();
	
	private int chainLength;
	
	public MarkovGenerator(int cl) {
		chainLength=cl;
	}
	
	public void addSample(String ss, Character c) {
		ProbabilityPicker<Character> pp=chances.get(ss);
		if (pp==null) {
			pp=new ProbabilityPicker<Character>();
			chances.put(ss,pp);
		}
		pp.add(c, 1);
	}
	
	public void load(String s) {
		// TODO
		for (int i=0; i<s.length(); i++) {
			String ss=s.substring(Maths.max(0,i-chainLength),i);
			Character c=s.charAt(i);
			addSample(ss,c);
		}
	}
	
	public String generate(int length) {
		StringBuilder sb=new StringBuilder();
		for (int i=0; i<length; i++) {
			String ss=sb.substring(Maths.max(0,i-chainLength),i);
			Character c=chances.get(ss).pick();
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		MarkovGenerator mg=new MarkovGenerator(8);
		String s=TextUtils.loadFromFile("/mikera/words/sampletext.txt");
		mg.load(s);
		System.out.println(mg.generate(1000));
	}
}
