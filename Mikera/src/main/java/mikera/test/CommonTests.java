package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

import mikera.data.Data;
import mikera.math.*;
import mikera.net.*;
import mikera.net.DataInputStream;
import mikera.net.DataOutputStream;
import mikera.persistent.*;
import mikera.persistent.impl.RepeatList;
import mikera.util.Rand;

public class CommonTests {
	
	@Test public void testCommonData() {
		testCommonData(new Pair<Integer, String>(1,"Hello"));
		testCommonData(new Data());
		testCommonData(Integer.valueOf(3));
		testCommonData(Short.valueOf((short)3));
		testCommonData(0.7543245);
		testCommonData(new Vector(3));
		testCommonData(new Point3i(3,4,5));
		testCommonData(Text.create("Hello serialized world!"));
		testCommonData(RepeatList.create("Spam ",100));
		testCommonData(IntSet.create(new int[]{1,4,6,7}));		
		testCommonData(Rand.getGenerator());
	}
	
	public static void testCommonData(Object o) {
		if (o instanceof Serializable) {
			testSerialization((Serializable)o);
		}
	}

	
	public static void testSerialization(Serializable s) {
		
		Data d=new Data();
		DataOutputStream dos=new DataOutputStream(d);
		DataInputStream dis=new DataInputStream(d);
		
		try {
			ObjectOutputStream oos=new ObjectOutputStream(dos);
			ObjectInputStream ois=new ObjectInputStream(dis);
			
			oos.writeObject(s);
			
			Serializable s2=(Serializable)ois.readObject();

			assertEquals(s,s2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}		
	}
}
