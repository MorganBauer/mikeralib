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
import mikera.persistent.impl.SingletonList;
import mikera.persistent.impl.Tuple;
import mikera.util.*;
import mikera.util.emptyobjects.*;

import java.util.*;

public class TestPersistentMap {
	@Test public void testBitMap() {
		assertEquals(2,PersistentHashMap.PHMBitMapNode.indexFromSlot(8, 0x00001111));
		assertEquals(0,PersistentHashMap.PHMBitMapNode.indexFromSlot(8, 0x00001100));
		
		assertEquals(3,PersistentHashMap.PHMBitMapNode.slotFromHash(0x00170030,4));
		assertEquals(1,PersistentHashMap.PHMBitMapNode.slotFromHash(0x00170030,20));
	}
	
	@Test public void testMaps() {
		PersistentMap<Integer,String> pm=new PersistentHashMap<Integer,String>();
		testMap(pm);
	}
	
	@Test public void testMerge() {
		PersistentMap<Integer,String> pm=new PersistentHashMap<Integer,String>();
		pm=pm.include(1, "Hello");
		pm=pm.include(2, "World");
		
		PersistentMap<Integer,String> pm2=new PersistentHashMap<Integer,String>();
		pm2=pm2.include(2, "My");
		pm2=pm2.include(3, "Good");
		pm2=pm2.include(4, "Friend");

		PersistentMap<Integer,String> mm=pm.include(pm2);
		assertEquals(4,mm.size());
	}
	

	
	@Test public void testChanges() {
		PersistentMap<Integer,String> pm=new PersistentHashMap<Integer,String>();
		pm=pm.include(1, "Hello");
		pm=pm.include(2, "World");
		
		assertEquals(null,pm.get(3));
		assertEquals("Hello",pm.get(1));
		assertEquals("World",pm.get(2));
		assertEquals(2,pm.size());
		
		pm.validate();
		pm=pm.include(2, "Sonia");
		pm.validate();
		assertEquals("Hello",pm.get(1));
		assertEquals("Sonia",pm.get(2));
		assertEquals(2,pm.size());

		pm=pm.delete(1);
		assertEquals(null,pm.get(1));
		assertEquals("Sonia",pm.get(2));
		assertEquals(1,pm.size());		
		
		assertTrue(pm.values().contains("Sonia"));
		assertTrue(pm.keySet().contains(2));
		
		testMap(pm);
	}
	
	public void testMap(PersistentMap<Integer,String> pm) {
		pm.validate();
		testIterator(pm);
		testRandomAdds(pm);
	}
	
	public void testIterator(PersistentMap<Integer,String> pm) {
		int i=0;
		for (Map.Entry<Integer,String> ent: pm.entrySet()) {
			assertTrue(pm.containsKey(ent.getKey()));
			assertTrue(Tools.equalsWithNulls(ent.getValue(), pm.get(ent.getKey())));
			i++;
		}
		assertEquals(pm.size(),i);
	}
	
	public void testRandomAdds(PersistentMap<Integer,String> pm) {
		pm=addRandomStuff(pm,100,1000000);
		int size=pm.size();
		assertTrue(size>90);
		assertEquals(size,pm.entrySet().size());
		assertEquals(size,pm.keySet().size());
		assertEquals(size,pm.values().size());	
	}
	
	public PersistentMap<Integer,String> addRandomStuff(PersistentMap<Integer,String> pm, int n , int maxIndex ) {
		for (int i=0; i<n; i++) {
			pm=pm.include(Rand.r(maxIndex),Rand.nextString());
		}
		return pm;
	}
	
	@Test public void testManyChanges() {
		PersistentMap<Integer,String> pm=new PersistentHashMap<Integer,String>();
		pm=addRandomStuff(pm,1000,40);
		assertEquals(40,pm.size());
		testMap(pm);
	}

}