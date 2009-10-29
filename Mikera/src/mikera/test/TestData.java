package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.net.Data;
import mikera.util.*;

import java.nio.ByteBuffer;
import java.util.*;

public class TestData {
	@Test public void testData1() {
		Data d=new Data();
		
		d.append((byte) 1);
		d.append((byte) 2);
		assertEquals(1,d.getByte(0));
		assertEquals(2,d.getByte(1));
		assertEquals(2,d.size());
		
		Data dd=d.clone();
		dd.append(d);
		assertEquals(4,dd.size());
		assertEquals(2,dd.getByte(3));
		
		try {
			dd.get(10);
			fail();
		} catch (IndexOutOfBoundsException x) {
			// OK!
		}
		
		try {
			dd.get(-1);
			fail();
		} catch (IndexOutOfBoundsException x) {
			// OK!
		}
		
		byte[] bs=dd.toNewByteArray();
		assertEquals(2,bs[3]);
		assertEquals(4,bs.length);
		
		ByteBuffer bb=dd.toFlippedByteBuffer();
		assertEquals(4,bb.remaining());
		
		ByteBuffer bb2=dd.toWrapByteBuffer();
		assertEquals(4,bb2.remaining());

		Data d3=Data.create(bb2);
		assertEquals(4,d3.size());
		assertEquals(2,d3.getByte(3));
		
	}
	
	@Test public void testData2() {
		Data d=new Data();
		d.appendInt(1000);
		d.appendInt(-2000);

		assertEquals(1000,d.getInt(0));
		assertEquals(-2000,d.getInt(4));
		
		long lv=Rand.nextLong();
		d.appendLong(lv);
		assertEquals(lv,d.getLong(8));
		
		float fv=Rand.nextFloat();
		d.appendFloat(fv);
		assertEquals(fv,d.getFloat(16),0);
		
		double dv=Rand.nextDouble();
		d.appendDouble(dv);
		assertEquals(dv,d.getDouble(20),0);

		char cv=(char)Rand.nextInt();
		d.appendChar(cv);
		assertEquals(cv,d.getChar(28),0);
	
		assertEquals(30,d.size());
	}
}
