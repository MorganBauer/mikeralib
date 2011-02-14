package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.data.BigRational;

public class TestBigRational {
	@Test public void testBR() {
		BigRational b1=new BigRational(1);
		BigRational b2=new BigRational(1,1);
		
		assertEquals(b1,b2);
	}
}
