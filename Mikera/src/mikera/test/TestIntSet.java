package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;

public class TestIntSet {
	@Test public void test1() {
		int[] array=new int[] {0 , 5, -6, 100, -50, 10, -2};
		IntSet is1=IntSet.create(array);
		
		assertEquals(-50,is1.toIntArray()[0]);
		assertEquals(0,is1.findIndex(-50));
		assertEquals(true,is1.contains(-6));
		assertEquals(false,is1.contains(-7));
		
		assertFalse(is1.hasDuplicates());
	}
	
	@Test public void test2() {
		int[] array=new int[] {1,2,3};
		IntSet is1=IntSet.create(array);
		IntSet is2=null;
		
		for (int i=0; i<50; i++) {
			int v=Rand.r(50);
			is2=IntSet.createMerged(is1, v);
			assertTrue(is2.contains(is1));
			assertTrue(is2.contains(v));
			assertFalse(is2.hasDuplicates());
			is1=is2;
		}
		
		int c=0;
		for (Integer v: is1) {
			assertTrue(is1.contains(v));
			assertEquals(c,is1.findIndex(v));
			c++;
		}
		assertTrue(is1.size()==c);
	}
	
	@Test public void test3() {
		int[] array=new int[] {1,2,3};
		IntSet is1=IntSet.create(array);
		IntSet is2=IntSet.create(array);
		IntSet is3=is1.clone();

		assertTrue(is1==is2); // should get cached version
		assertTrue(is1!=is3);
		assertTrue(is1.equals(is3));
	}
}
