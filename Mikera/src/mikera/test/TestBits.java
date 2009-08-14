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
		assertEquals(1,bg.dataLength());
		
		bg.set(1,1,1,1);
		assertEquals(0,bg.get(0, 0, 0));
		bg.set(0,0,0,1);
		assertEquals(1,bg.get(0, 0, 0));
		bg.set(0,0,0,0);
		assertEquals(0,bg.get(0, 0, 0));
		assertEquals(1,bg.get(1, 1, 1));
		assertEquals(1,bg.countSetBits());
		
		bg.set(-10,-10,-10,1);
		assertEquals(0,bg.get(0, 0, 0));
		assertEquals(1,bg.get(-10, -10, -10));
		assertEquals(1,bg.get(1, 1, 1));
		assertEquals(2,bg.countSetBits());
		
		bg.clear();
		assertEquals(0,bg.dataLength());
		bg.set(Rand.d(100),Rand.d(100),-Rand.d(100),1);
		assertEquals(1,bg.dataLength());
	}
	
	public class TestVisitor extends PointVisitor<Integer> {
		int count=0;
		int tcount=0;
		@Override
		public boolean visit(int x, int y, int z, Integer value) {
			count+=value;
			tcount++;
			return false;
		}		
	}
	
	@Test public void testCountBits() {
		assertEquals(1,Bits.countSetBits(0x00010000));
		assertEquals(32,Bits.countSetBits(-1));
		assertEquals(8,Bits.countSetBits(0xF000000F));

	}
	
	@Test public void testBitGridVisitors() {
		BitGrid bg=new BitGrid(0,0,0);
		
		bg.set(0,0,0,1);
		bg.set(10,10,-10,1);
		
		TestVisitor pv=new TestVisitor();
		
		bg.visitBits(pv);
		assertEquals(2,pv.count);
		assertTrue(2<pv.tcount);
		

	}
}
