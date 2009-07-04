package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;

public class TestMaths {
	@Test public void testClamp() {
		assertEquals(1,Maths.clamp(0,1,2));
		assertEquals(10,Maths.clamp(10,0,20));
		assertEquals(20,Maths.clamp(100,0,20));
	}
	

}
