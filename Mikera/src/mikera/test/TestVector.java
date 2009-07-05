package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.engine.*;
import mikera.util.*;

public class TestVector {
	@Test public void testInit() {
		Vector v=new Vector(0,1,2);
		v.scale(2);
		assertEquals("{0.0, 2.0, 4.0}",v.toString());
	}
	

}
