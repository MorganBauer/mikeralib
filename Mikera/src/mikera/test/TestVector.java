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
	

	@Test public void testNormalise() {
		Vector v=new Vector(6,0,0);
		
		float len=v.normalise();
		assertEquals("{1.0, 0.0, 0.0}",v.toString());
		assertEquals(6,len);
	}
	
	@Test public void testRotation() {
		Vector v=new Vector(1,0,0);
		Vector v2=new Vector(3,0,0);
		
		Matrix m=new Matrix(3,3);
		
		m.setToRotation3(Rand.nextFloat()-0.5f, Rand.nextFloat()-0.5f, Rand.nextFloat()-0.5f, Rand.nextFloat()*10-5);
		
		Matrix.multiplyVector(m,v,v2);
		
		assertEquals(1.0f,v2.length(),0.0001f);
	}
}
