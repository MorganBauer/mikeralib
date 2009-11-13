package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.persistent.ListFactory;
import mikera.persistent.PersistentCollection;
import mikera.persistent.PersistentHashMap;
import mikera.persistent.PersistentList;
import mikera.persistent.PersistentMap;
import mikera.persistent.impl.CompositeList;
import mikera.persistent.impl.RepeatList;
import mikera.persistent.impl.Singleton;
import mikera.persistent.impl.Tuple;
import mikera.util.*;
import mikera.util.emptyobjects.*;

import java.util.*;

public class TestPersistentHash {
	
	@Test public void testMap() {
		PersistentMap<Integer,String> pm=new PersistentHashMap<Integer,String>();

		pm=pm.include(1, "Hello");
		pm=pm.include(2, "World");
		
		assertEquals(null,pm.get(3));
		assertEquals("Hello",pm.get(1));
		assertEquals("World",pm.get(2));
		assertEquals(2,pm.size());
		
		pm=pm.include(2, "Sonia");
		assertEquals("Hello",pm.get(1));
		assertEquals("Sonia",pm.get(2));
		assertEquals(2,pm.size());

		pm=pm.delete(1);
		assertEquals(null,pm.get(1));
		assertEquals("Sonia",pm.get(2));
		assertEquals(1,pm.size());

	}

}