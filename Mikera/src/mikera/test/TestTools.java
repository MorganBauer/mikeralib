package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;

public class TestTools {
	@Test public void testSort() {
		Integer[] a=new Integer[100];
		Integer[] b=new Integer[100];
		
		for (int i=0; i<100; i++) {
			a[i]=Rand.d(100);
		}
		
		Tools.mergeSort(a, b, 0, 99);
		
		assertTrue(Arrays.isSorted(a, 0, 99));
	}
}
