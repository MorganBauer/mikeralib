package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.engine.SparseGrid;
import mikera.engine.SparseArray;
import mikera.util.*;

public class TestSparse {
	@Test public void testSparseArray() {
		
		SparseArray<Integer> sa=new SparseArray<Integer>();
		
		sa.set(0, 1);
		assertEquals(1,(int)sa.get(0));
		
		sa=sa.grow();
		assertEquals(1,(int)sa.get(0));
		
	}
	
	@Test public void testSparseGrid() {
		assertEquals(29120,SparseGrid.calculateIndex(10, 10, 10));
	}

}
