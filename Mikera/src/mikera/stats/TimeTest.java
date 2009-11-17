package mikera.stats;

import java.util.*;
import mikera.engine.*;
import mikera.persistent.*;
import mikera.util.*;

/**
 * Testing class to test a specific function
 * @author Mike
 *
 */
public class TimeTest {

	public static final int PRERUNS=100;
	public static final int RUNS=100;
	private static int count=0;
	
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

		System.out.println("time = "+TextUtils.leftPad(Long.toString((atime-btime)/RUNS),12)+" ns");
	}
	
	static Random rand=new Random();
	private static void setup() {
		
	}
	
	private static void a() {
		//long l=rand.nextLong();
		count++;
	}
	
	private static void b() {
		count++;
	}
}
