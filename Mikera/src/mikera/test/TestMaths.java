package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.math.PerlinNoise;
import mikera.util.*;
import mikera.engine.*;

public class TestMaths {
	@Test public void testClamp() {
		assertEquals(1,Maths.clamp(0,1,2));
		assertEquals(10,Maths.clamp(10,0,20));
		assertEquals(20,Maths.clamp(100,0,20));
	}
	
	@Test public void testPerlinNoise() {
		PerlinNoise p=new PerlinNoise();
		
		float min=10;
		float max=-10;
		for (int i=0; i<1000; i++) {
			float f=p.noise3(Rand.n(0,10),Rand.n(0,10),Rand.n(0,10));
			min=Math.min(f,min);
			max=Math.max(f,max);
		}
		
		//System.err.println(min);
		//System.err.println(max);
	}
	
	@Test public void testMod() {
		assertEquals(0.2f,Maths.fmod(1.2f, 1.0f),0.00001f);
		assertEquals(0.2f,Maths.fmod(-0.8f, 1.0f),0.00001f);

		assertEquals(0.2f,Maths.fmod(1.6f, 1.4f),0.00001f);
		assertEquals(0.0f,Maths.fmod(1.6f, 1.6f),0.00001f);

		
		assertEquals(2.0f,Maths.fmod(-8f, 10.0f),0.00001f);

		
	}
	
	@Test public void testIntMod() {
		assertEquals(2,Maths.mod(2, 5));
		assertEquals(2,Maths.mod(7, 5));
		assertEquals(1,Maths.mod(-7, 8));
		assertEquals(0,Maths.mod(10, 10));

		
	}
	
	@Test public void testAbs() {
		assertEquals(0.0f,Maths.abs(0.0f),0.00001f);
	}
	
	@Test public void testFastSqrt() {
		assertEquals(0.5,Maths.fastInverseSqrt(4),0.01f);
		
		// System.out.println(Maths.alternateSqrt(33f)+" vs."+Maths.sqrt(33f));
	}
	
	@Test public void testFloor() {
		assertEquals(0,Maths.floor(0));
		assertEquals(1,Maths.floor(1));
		assertEquals(-1,Maths.floor(-1));
		assertEquals(1,Maths.floor(1.2));
		assertEquals(-1,Maths.floor(-0.0001));
		assertEquals(-1,Maths.floor(-0.9001));
		
	}
	
	@Test public void testSpeed1() {
		for (int i=0; i<1000000; i++) {
			float f=Maths.sqrt(i);
			assertEquals(f,f,0.01f);
		}
	}
	
	@Test public void testSpeed2() {
		for (int i=0; i<1000000; i++) {
			float f=Maths.alternateSqrt(i);
			assertEquals(f,f,0.01f);
		}
	}
}
