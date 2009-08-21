package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.engine.*;
import mikera.math.*;
import java.util.*;

public class TestRayCaster {
	@Test public void testPoints() {
		HashSet<Point3i> hm=new HashSet<Point3i>();
		
		hm.add(new Point3i(1,1,1));
		hm.add(new Point3i(1,1,1));
		hm.add(new Point3i(1,1,1));
		hm.add(new Point3i(1,1,1));
		hm.add(new Point3i(10,10,10));
		hm.add(new Point3i(10,10,10));
		
		assertEquals(2,hm.size());
		assertEquals(new Point3i(1,1,1),new Point3i(1,1,1));
	}
	
	@Test public void testInit() {
		RayCaster rc=new RayCaster();
		
		RayCaster.CastNode cn=rc.generatePaths(1);
		assertEquals(6,cn.countDistinctPaths());
		assertEquals(22,cn.countCache());
		
		cn=rc.generatePaths(2);
		assertEquals(26,cn.countDistinctPaths());
	}
}
