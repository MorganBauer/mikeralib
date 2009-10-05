package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.persistent.ListFactory;
import mikera.persistent.PersistentList;
import mikera.persistent.Singleton;
import mikera.persistent.Tuple;
import mikera.util.*;
import mikera.util.emptyobjects.*;

import java.util.*;

public class TestPersistent {
	
	@SuppressWarnings("unchecked")
	@Test public void testTypes() {
		
		PersistentList<Integer> pl=ListFactory.create(new Integer[] {1,2,3,4,5});
		assertEquals(5,pl.size());
		testPersistentList(pl);
		
		testPersistentList(NullList.INSTANCE);
		testPersistentList(Tuple.create(new Integer[] {1,2,3,4,5}));
		testPersistentList(Singleton.create("Hello persistent lists!"));
	}
	
	public <T> void testPersistentList(PersistentList<T> a) {
		testSubLists(a);
		testAppends(a);
		testConcats(a);
		testCuts(a);
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
}