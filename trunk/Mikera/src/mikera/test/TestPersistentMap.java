package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.persistent.ListFactory;
import mikera.persistent.MapFactory;
import mikera.persistent.PersistentCollection;
import mikera.persistent.PersistentHashMap;
import mikera.persistent.PersistentList;
import mikera.persistent.PersistentMap;
import mikera.persistent.PersistentSet;
import mikera.persistent.SetFactory;
import mikera.persistent.impl.CompositeList;
import mikera.persistent.impl.RepeatList;
import mikera.persistent.impl.SingletonList;
import mikera.persistent.impl.Tuple;
import mikera.util.*;
import mikera.util.emptyobjects.*;

import java.util.*;

public class TestPersistentMap {
	@Test public void testBitMapFunctions() {
		assertEquals(2,PersistentHashMap.PHMBitMapNode.indexFromSlot(8, 0x00001111));
		assertEquals(0,PersistentHashMap.PHMBitMapNode.indexFromSlot(8, 0x00001100));
		
		assertEquals(3,PersistentHashMap.PHMBitMapNode.slotFromHash(0x00170030,4));
		assertEquals(1,PersistentHashMap.PHMBitMapNode.slotFromHash(0x00170030,20));
	}
	
	@Test public void testMaps() {
		PersistentMap<Integer,String> pm=new PersistentHashMap<Integer,String>();
		testMap(pm);
	}
	
	@Test public void testConvert() {
		PersistentMap<Integer,String> phm=new PersistentHashMap<Integer,String>();

		HashMap<Integer,String> hm=new HashMap<Integer,String>();
		for (int i=0; i<100; i++) {
			int key=Rand.r(100);
			String value=Rand.nextString();
			hm.put(key, value);
			phm=phm.include(key,value);
			
			int delKey=Rand.r(100);
			hm.remove(delKey);
			phm=phm.delete(delKey);
		}
		testMap(phm);
		
		PersistentMap<Integer,String> pm=MapFactory.create(hm);
		testMap(pm);
		
		HashMap<Integer,String> hm2=pm.toHashMap();
		assertEquals(hm,hm2);
		
		PersistentSet<Integer> ks=SetFactory.create(hm.keySet());
		PersistentSet<Integer> ks2=pm.keySet();
		PersistentSet<Integer> ks3=phm.keySet();
		assertEquals(ks,ks2);
		assertEquals(ks,ks3);
		
		PersistentList<String> vs=ListFactory.create(hm.values());
		PersistentList<String> vs2=ListFactory.create(pm.values());
		PersistentList<String> vs3=ListFactory.create(phm.values());
		assertEquals(SetFactory.create(vs),SetFactory.create(vs2));
		assertEquals(SetFactory.create(vs),SetFactory.create(vs3));
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
	
	@Test public void testToString() {
		HashMap<Integer,String> hm=new HashMap<Integer, String>();
		hm.put(1, "Hello");
		hm.put(2, "World");
		
		PersistentMap<Integer,String> pm=PersistentHashMap.create(1,"Hello");
		pm=pm.include(2,"World");
		assertEquals(hm.toString(),pm.toString());
		assertEquals("{1=Hello, 2=World}",pm.toString());
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
		CommonTests.testCommonData(pm);
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