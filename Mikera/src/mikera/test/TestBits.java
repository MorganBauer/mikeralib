package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.*;
import mikera.engine.*;

public class TestBits {
	@Test public void testRoundUp() {
		assertEquals(16,Bits.roundUpToPowerOfTwo(15));
		assertEquals(16,Bits.roundUpToPowerOfTwo(16));
		assertEquals(32,Bits.roundUpToPowerOfTwo(17));
		assertEquals(1,Bits.roundUpToPowerOfTwo(1));
		assertEquals(0,Bits.roundUpToPowerOfTwo(0));
	}
	
	@Test public void testRoundDown() {
		assertEquals(16,Bits.roundDownToPowerOfTwo(17));
		assertEquals(16,Bits.roundDownToPowerOfTwo(31));
		assertEquals(32,Bits.roundDownToPowerOfTwo(33));
		assertEquals(2,Bits.roundDownToPowerOfTwo(2));
		assertEquals(1,Bits.roundDownToPowerOfTwo(1));
		assertEquals(0,Bits.roundDownToPowerOfTwo(0));
	}
	
	@Test public void testSigBits() {
		assertEquals(1,Bits.significantBits(0));
		assertEquals(1,Bits.significantBits(-1));

		assertEquals(4,Bits.significantBits(4));
		assertEquals(3,Bits.significantBits(3));
		
		assertEquals(3,Bits.significantBits(-4));

	}
	
	@Test public void testBitGrid1() {
		BitGrid bg=new BitGrid(0,0,0);
		assertEquals(4,bg.width());
		assertEquals(4,bg.height());
		assertEquals(2,bg.depth());
		assertEquals(21,bg.bitPos(1, 1, 1));
		
		
		bg.set(1,1,1,1);
		assertEquals(0,bg.get(0, 0, 0));
		bg.set(0,0,0,1);
		assertEquals(1,bg.get(0, 0, 0));
		bg.set(0,0,0,0);
		assertEquals(0,bg.get(0, 0, 0));
		assertEquals(1,bg.get(1, 1, 1));
		
	}
}
