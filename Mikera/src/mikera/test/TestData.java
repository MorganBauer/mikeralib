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
		assertEquals(1,d.get(0));
		assertEquals(2,d.get(1));
		assertEquals(2,d.size());
		
		Data dd=d.clone();
		dd.append(d);
		assertEquals(4,dd.size());
		assertEquals(2,dd.get(3));
		
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
		assertEquals(2,d3.get(3));
		
	}
}
