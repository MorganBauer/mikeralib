package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;

public class TestPrefixTree {
	@Test public void testPT() {
		PrefixTree<Integer,String> pt=new PrefixTree<Integer,String>();
		
		Integer[] a1=new Integer[] {1,2,3};
		pt.add(a1,"A");
		assertEquals(3,pt.countNodes());
		assertEquals(1,pt.countValues());
		assertEquals(1,pt.countLeaves());
		
		Integer[] a2=new Integer[] {1,2,4};
		pt.add(a2,"B");
		Integer[] a3=new Integer[] {1,2,4,5};
		pt.add(a3,"C");
		assertEquals(5,pt.countNodes());
		assertEquals(3,pt.countValues());
		assertEquals(2,pt.countLeaves());
	}
}
