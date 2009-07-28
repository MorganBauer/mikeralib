package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;

public class TestPrefixTree {
	@Test public void testPT() {
		PrefixTree<Integer,String> pt=new PrefixTree<Integer,String>();
		
		Integer[] a1=new Integer[] {1,2,3};
		pt.add(a1,"A");
		
		a1[2]=4;
		pt.add(a1,"B");
		
		assertEquals(5,pt.countNodes());
		assertEquals(2,pt.countValues());
		assertEquals(2,pt.countLeaves());
		
	}
}
