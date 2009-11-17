package mikera.stats;

import java.util.*;
import mikera.engine.*;
import mikera.persistent.*;
import mikera.util.*;

public class PerformanceTest {

	public static final int PRERUNS=10;
	public static final int RUNS=100;
	
	public static void main(String[] args) {
		setup();
		
		for (int i=0; i<PRERUNS; i++) {
			a();
			b();
		}
		
		long astart=System.nanoTime();
		for (int i=0; i<RUNS; i++) {
			a();
		}		
		long atime=System.nanoTime()-astart;
		
		long bstart=System.nanoTime();
		for (int i=0; i<RUNS; i++) {
			b();
		}		
		long btime=System.nanoTime()-bstart;

		System.out.println("a time = "+TextUtils.leftPad(Long.toString(atime/RUNS),12)+" ns");
		System.out.println("b time = "+TextUtils.leftPad(Long.toString(btime/RUNS),12)+" ns");
	}
	
	/**
	 * Setup code here
	 */
	
	/*
	static Octreap<Integer> o1=new Octreap<Integer>();
	static Grid<Integer> g1;
	static Grid<Integer> g2;
	private static void setup() {
		g1=new Octreap<Integer>();
		g2=new TreeGrid<Integer>();
	}
	*/
	
	/**
	 * Enter code to time here
	 */
	
	/*
	private static void a() {
		g1.set(0,0,0,1);
		g1.get(0,0,0);
		//testGrid(g1);
	}
	
	private static void b() {
		g2.set(0,0,0,1);
		g2.get(0,0,0);
		//testGrid(g2);
	}
	
	public static void testGrid(Grid<Integer> g) {
		g.clear();
		g.setBlock(0, 0, 0, 100, 100, 100, 1);
		g.setBlock(0, 0, 0, 3, 3, 3, 2);
		g.set(2,2,2,3);
	}
	
	*/
	
	static HashMap<Integer,String> hm;
	static PersistentMap<Integer,String> pm;
	private static void setup() {
		hm=new HashMap<Integer,String>();
		pm=new PersistentHashMap<Integer,String>();
	}
	
	private static void a() {
		hm.clear();
		for (int i=0; i<100; i++) {
			int key=Rand.r(100);
			String value=Rand.nextString();
			hm.put(key, value);
			
			int delKey=Rand.r(100);
			hm.remove(delKey);
			
			for (int l=0; l<50; l++) {
				hm.get(Rand.r(100));
			}
			
			for (String s: hm.values()) {
				if (s.equals("fvfieuvgfeoivboi")) throw new Error();
			}
			hm.clone();
		}
	}
	
	private static void b() {
		pm=new PersistentHashMap<Integer,String>();
		for (int i=0; i<100; i++) {
			int key=Rand.r(100);
			String value=Rand.nextString();
			pm=pm.include(key,value);
			
			int delKey=Rand.r(100);
			pm=pm.delete(delKey);
			
			for (int l=0; l<50; l++) {
				pm.get(Rand.r(100));
			}
			
			for (String s: pm.values()) {
				if (s.equals("fvfieuvgfeoivboi")) throw new Error();				
			}
			pm.clone();
		}
	}
}