package mikera.util;

import java.util.*;

public class PrefixTree<T,V> {
	private T head;
	private V value;
	private ArrayList<PrefixTree<T,V>> tails;
	
	// TODO
	
	public PrefixTree() {
		
	}
	
	public int countNodes() {
		if (tails==null) return 1;
		int result=1;
		for (PrefixTree<T, V> pt:tails) {
			result+=pt.countNodes();
		}
		return result;
	}
	
	public int countValues() {
		int result=(value==null)?0:1;
		if (tails==null) return result;
		for (PrefixTree<T, V> pt:tails) {
			result+=pt.countValues();
		}
		return result;
	}
	
	public int countLeaves() {
		if ((tails==null)||tails.isEmpty()) return 1;
		int result=0;
		for (PrefixTree<T, V> pt:tails) {
			result+=pt.countLeaves();
		}
		return result;
	}
	
	public void add(T[] ts) {
		add(ts,0,ts.length,null);
	}
	
	public void add(T[] ts, V newValue) {
		add(ts,0,ts.length,newValue);
	}
	
	public void add(T[] ts, int offset, int length, V newValue) {
		if (length<=0) return;
		T item=ts[offset];
		PrefixTree<T,V> branch=getBranch(item);
		if (branch==null) {
			branch=addBranch(item);
		}
		if (length==1) {
			branch.value=newValue;
		} else {
			branch.add(ts, offset+1, length-1,newValue);
		}
	}
	
	public PrefixTree<T,V> getBranch(T a) {
		if (tails==null) return null;
		for (PrefixTree<T,V> t:tails) {
			if ((a!=null)&&a.equals(t.head)) {
				return t;
			} else if ((a==null)&&(t.head==null)) {
				return t;
			}
		}
		return null;
	}
	
	private void ensureTails() {
		if (tails==null) {
			tails=new ArrayList<PrefixTree<T,V>>();
		}
	}
	
	private PrefixTree<T,V> addBranch(T a) {
		PrefixTree<T,V> t=new PrefixTree<T,V>();
		t.head=a;
		ensureTails();
		tails.add(t);
		return t;
	}
}
