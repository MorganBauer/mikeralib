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
	}
	
	public void testEmptyGrid(Grid<Integer> g) {
		assertEquals(null,g.get(0, 0, 0));
		assertEquals(null,g.get(-10, -10, -10));
	}
	
	public void testSet(Grid<Integer> g) {
		g.set(10,10,10, 1);
		g.set(-1,-1,-1, 1);
		assertEquals(1,(int)g.get(10, 10, 10));
		assertEquals(1,(int)g.get(-1, -1, -1));

		g.clear();
		assertEquals(null,g.get(0, 0, 0));
	}
}
