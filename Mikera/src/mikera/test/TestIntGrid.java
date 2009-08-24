package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;
import mikera.engine.*;

public class TestIntGrid {
	@Test public void test1() {
		IntGrid ig=new IntGrid();
		
		ig.set(0, 0, 0, 1);
		ig.set(10, 10, 10, 1);
		ig.set(-10, -10, -10, 1);
		assertEquals(1,ig.get(0,0,0));
		assertEquals(1,ig.get(10,10,10));
		assertEquals(1,ig.get(-10,-10,-10));
		assertEquals(0,ig.get(5,5,5));
		
		assertEquals(3,ig.countNonZero());	
		assertEquals(21*21*21,ig.dataLength());
	}
}
