package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.persistent.ListFactory;
import mikera.persistent.PersistentCollection;
import mikera.persistent.PersistentList;
import mikera.persistent.impl.CompositeList;
import mikera.persistent.impl.RepeatList;
import mikera.persistent.impl.Singleton;
import mikera.persistent.impl.Tuple;
import mikera.util.*;
import mikera.util.emptyobjects.*;

import java.util.*;

public class TestPersistent {
	
	@SuppressWarnings("unchecked")
	@Test public void testListTypes() {
		
		PersistentList<Integer> pl=ListFactory.create(new Integer[] {1,2,3,4,5});
		assertEquals(5,pl.size());
		
		testPersistentList(pl);
		testPersistentList(pl.subList(1, 4));
		testPersistentList(NullList.INSTANCE);
		testPersistentList(Tuple.create(new Integer[] {1,2,3,4,5}));
		testPersistentList(Singleton.create("Hello persistent lists!"));
		testPersistentList(RepeatList.create("Hello", 1000));
		testPersistentList(CompositeList.create(pl));
	}
	
	@Test public void testCollectionTypes() {
		testPersistentCollection(NullList.INSTANCE);
		testPersistentCollection(NullCollection.INSTANCE);
		testPersistentCollection(NullSet.INSTANCE);
	}
	
	public <T> void testPersistentCollection(PersistentCollection<T> a) {
		testDelete(a);
		testInclude(a);
		testClone(a);
		testSizing(a);

	}
	
	public <T> void testSizing(PersistentCollection<T> a) {
		assertTrue(a.size()>=0);
	}
	
	public <T> void testDelete(PersistentCollection<T> a) {
		if (a==null) throw new Error("!!!");
		PersistentCollection<T> da=a.deleteAll(a);
		assertEquals(0,da.size());
		assertTrue(da.isEmpty());
		assertEquals(0,da.hashCode());
	}
	
	public <T> void testClone(PersistentCollection<T> a) {
		PersistentCollection<T> ca=a.clone();
		assertTrue(ca.equals(a));
		assertTrue(ca.hashCode()==a.hashCode());
		assertTrue(ca.getClass()==a.getClass());
		//assertTrue(ca!=a);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void testInclude(PersistentCollection<T> a) {
		T[] ar=(T[])a.toArray();
		if (ar.length>0) {
			T v=ar[Rand.r(ar.length)];
			assertTrue(a.contains(v));
			
			PersistentCollection<T> ad=a.deleteAll(v);
			assertFalse(ad.contains(v));
			
			PersistentCollection<T> adi=ad.include(v);		
			assertTrue(adi.contains(v));
			assertTrue(adi.containsAll(a));

			PersistentCollection<T> adii=adi.include(v);
			assertEquals(adii,adi);
		}
	}
	
	public <T> void testPersistentList(PersistentList<T> a) {
		testSubLists(a);
		testAppends(a);
		testConcats(a);
		testCuts(a);
		testDeletes(a);
		testEquals(a);
		testInserts(a);
		testExceptions(a);
		testPersistentCollection(a);
		testFrontBack(a);

	}

	public <T> void testFrontBack(PersistentList<T> a) {
		PersistentList<T> f=a.front();
		PersistentList<T> b=a.back();
		
		assertEquals(a.size(),f.size()+b.size());
		assertEquals(a,f.append(b));
	}
	
	public <T> void testExceptions(PersistentList<T> a) {
		try {
			a.get(-1);
			fail();
		} catch (IndexOutOfBoundsException x) {/* OK */}
		
		try {
			a.get(a.size());
			fail();
		} catch (IndexOutOfBoundsException x) {/* OK */}
		
		try {
			// negative delete range
			a.delete(2,0);
			fail();
		} catch (Exception x) {/* OK */}
		
		try {
			// negative delete position
			a.delete(-3,2);
			fail();
		} catch (Exception x) {/* OK */}

		try {
			// out of range delete
			a.delete(0,1000000000);
			fail();
		} catch (Exception x) {/* OK */}

		try {
			// negative delete position
			a.delete(-4,-4);
			fail();
		} catch (Exception x) {/* OK */}

		try {
			// clear persistent object
			a.clear();
			if (a.size()>=0) fail();
		} catch (UnsupportedOperationException x) {/* OK */}

	}

	
	@SuppressWarnings("unchecked")
	public <T> void testEquals(PersistentList<T> a) {
		assertEquals(a,a.clone());
		assertEquals(a,a.append((PersistentList<T>)NullList.INSTANCE));
		assertEquals(a,a.delete(0,0));
	}
	
	public <T> void testDeletes(PersistentList<T> a) {
		int start=Rand.r(a.size());
		int end=Rand.range(start, a.size());
		
		PersistentList<T> sl=a.subList(start,end);
		
		PersistentList<T> dl=a.delete(start, end);
		if (start>0) {
			assertEquals(a.get(start-1),dl.get(start-1));
		}
		if (end<a.size()) {
			assertEquals(a.get(end),dl.get(start));
		}
		
		PersistentList<T> nl=dl.insert(start, sl);
		
		assertEquals(a,nl);
	}
	
	public <T> void testInserts(PersistentList<T> a) {
		int start=Rand.r(a.size());
		PersistentList<T> pl=a.insert(start, a);
		if (a.size()>0) {
			assertEquals(a.get(0),pl.get(start));
		}
	}

	public <T> void testCuts(PersistentList<T> a) {
		PersistentList<T> front=a.front();
		PersistentList<T> back=a.back();
		
		assertEquals(a.size(),front.size()+back.size());
	}
	
	public <T> void testConcats(PersistentList<T> a) {
		int n=a.size();
		PersistentList<T> pl=a;
		
		pl=ListFactory.concat(pl, pl);
		pl=ListFactory.concat(pl, pl);
		pl=ListFactory.concat(pl, pl);
		pl=ListFactory.concat(pl, pl);
		assertEquals(n*16,pl.size());
		
		if (n>0) {
			int r=Rand.r(pl.size());
			assertEquals(a.get(r%n),pl.get(r));
		}
		testSubLists(pl);
	}
	
	public <T> void testAppends(PersistentList<T> a) {
		ArrayList<T> al=new ArrayList<T>();
		
		int n=a.size();	
		for (int i=0; i<n; i++) {
			al.add(a.get(i));
		}
		
		PersistentList<T> la=Tuple.create(null, null);
		
		PersistentList<T> nl=la.append(a.append(la));
		assertEquals(n+4,nl.size());
		
		for (int i=0; i<n; i++) {
			assertTrue(Tools.equalsWithNulls(a.get(i), nl.get(i+2)));
		}
		
		// check hash code equivalence
		PersistentList<T> cp=Tuple.create(al);		
		assertEquals(cp.hashCode(),a.hashCode());
	}
	
	public <T> void testSubLists(PersistentList<T> a) {
		int n=a.size();
		for (int i=0; i<10; i++) {
			int b=Rand.r(n);
			int c=Rand.range(b, n);
			PersistentList<T> sl=a.subList(b, c);
			int sll=c-b;
			assertEquals(sll,sl.size());
			if (sll>0) {
				int r=Rand.range(0,sll-1);
				assertEquals(sl.get(r),a.get(b+r));
			}
		}
	}
	
	@Test public void testRepeats() {
		PersistentList<Integer> tl=(Tuple.create(new Integer[] {1,1,1,1,1}));
		PersistentList<Integer> rl=(RepeatList.create(1, 5));
		assertEquals(tl,rl);
	}
	
	@Test public void testDeleting() {
		PersistentList<Integer> tl=(Tuple.create(new Integer[] {1,2,3,4,5}));
		PersistentList<Integer> ol=(Tuple.create(new Integer[] {1,3,5}));
		PersistentList<Integer> pl=tl;
		pl=(PersistentList<Integer>) pl.deleteAll(2);
		pl=(PersistentList<Integer>) pl.deleteAll(4);
		assertEquals(ol,pl);
		assertEquals(ol,tl.delete(1).delete(2));
	}


}