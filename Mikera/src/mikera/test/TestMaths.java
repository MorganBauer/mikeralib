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
}
