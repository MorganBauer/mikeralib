package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;
import mikera.engine.*;

public class TestDirections {
	@Test public void testDirArrays() {
		byte[] td=new byte[27];
		
		for (int i=0; i<Dir.ALL_DIRECTIONS_3D.length; i++) {
			td[Dir.ALL_DIRECTIONS_3D[i]]++;
		}
		for (int i=0; i<Dir.DISTORDER_DIRECTIONS.length; i++) {
			td[Dir.DISTORDER_DIRECTIONS[i]]++;
		}
		
		for (int i=0; i<td.length; i++) {
			assertEquals(100*i+2, 100*i+td[i]);
		}
	}
	
	@Test public void testDirDirections() {
		Octreap<Integer> o=new Octreap<Integer>();
		
		for (int i=0; i<Dir.ALL_DIRECTIONS_3D.length; i++) {
			int d=Dir.ALL_DIRECTIONS_3D[i];
			int dx=Dir.DX[d];
			int dy=Dir.DY[d];
			int dz=Dir.DZ[d];
			o.set(dx,dy,dz,d);
		}
		assertEquals(27,o.countArea());
		
		// check distances are ordered
		for (int i=0; i<Dir.DISTORDER_DIRECTIONS.length; i++) {
			if (i>0) assertTrue(Dir.DIST[Dir.DISTORDER_DIRECTIONS[i]]>=Dir.DIST[Dir.DISTORDER_DIRECTIONS[i-1]]);
		}
		

		
	}
	
	@Test public void testDirCalcs() {
		for (int i=0; i<Dir.ALL_DIRECTIONS_3D.length; i++) {
			int d=Dir.ALL_DIRECTIONS_3D[i];
			int dx=Dir.DX[d];
			int dy=Dir.DY[d];
			int dz=Dir.DZ[d];

			int dcalc=Dir.getDir(dx, dy, dz);
			
			assertEquals(d,dcalc);
		}
	}
}
