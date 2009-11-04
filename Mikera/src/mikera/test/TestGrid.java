package mikera.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;

import mikera.engine.*;
import mikera.util.Rand;

import org.junit.Test;

public class TestGrid {
	@Test public void testAll() {
		testGrid(new Octreap<Integer>());
		testGrid(new ArrayGrid<Integer>());
		testGrid(new TreeGrid<Integer>());

	}
	
	public void testGrid(Grid<Integer> g) {
		testEmptyGrid(g);
		testSet(g);
		testSetBlock(g);
	}
	
	public void testEmptyGrid(Grid<Integer> g) {
		assertEquals(null,g.get(0, 0, 0));
		assertEquals(null,g.get(-10, -10, -10));
	}
	
	public void testSet(Grid<Integer> g) {
		g.set(10,10,10, 1);
		assertEquals(1,g.countNonNull());
		g.set(-1,-1,-1, 1);
		assertEquals(2,g.countNonNull());

		assertEquals(1,(int)g.get(10, 10, 10));
		assertEquals(1,(int)g.get(-1, -1, -1));

		g.clear();
		assertEquals(null,g.get(0, 0, 0));
	}
	
	public void testSetBlock(Grid<Integer> g) {
		g.setBlock(0,0,0,0,0,1,1);
		assertEquals(2,g.countNonNull());

		g.setBlock(0,0,0,1,1,1,1);
		assertEquals(8,g.countNonNull());

		g.setBlock(0,0,0,10,10,10,1);
		assertEquals(1331,g.countNonNull());
		
		assertEquals(1,(int)g.get(10, 10, 10));
		assertEquals(null,g.get(-1, -1, -1));
		assertEquals(1,(int)g.get(Rand.r(11), Rand.r(11), Rand.r(11)));
		
		g.setBlock(-5,-5,-5,5,5,5,2);
		assertEquals(1,(int)g.get(10, 10, 10));
		assertEquals(2,(int)g.get(0, 0, 0));
		assertEquals(2,(int)g.get(-1, -1, -1));

		g.setBlock(-2,-2,-2,2,2,2,null);
		assertEquals(2,(int)g.get(-3, -3, -3));
		assertEquals(null,g.get(-1, -1, -1));
		assertEquals(2,(int)g.get(3, 2, 2));
			
		g.clear();
	}
	
	@Test public void testTreeGidBlockSet() {
		TreeGrid<Integer> tg=new TreeGrid<Integer>();
		
		tg.set(0,0,0, 1);
	}
}
