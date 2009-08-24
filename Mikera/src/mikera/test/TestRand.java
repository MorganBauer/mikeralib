package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;
import mikera.engine.*;

public class TestRand {
	@Test public void testD() {
		int[] rs=new int [100];
		
		for (int i=0; i<1000; i++) {
			rs[Rand.d(20)]++;
		}
		
		assertEquals(0,rs[0]);
		assertEquals(0,rs[21]);
		
		for (int i=1; i<=20; i++) {
			assertTrue(rs[i]>1);
		}
	}
	
	@Test public void testPo() {
		int r=0;
		for (int i=0; i<1000; i++) {
			r+=Rand.po(1000);
		}
		assertTrue(r>500000);
	
	}
}
