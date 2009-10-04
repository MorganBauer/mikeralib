package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.persistent.PersistentList;
import mikera.persistent.Tuple;
import mikera.util.*;
import java.util.*;

public class TestPersistent {
	
	@Test public void testTypes() {
		
		
		testPersistentList(Tuple.create(new Integer[] {1,2,3,4,5}));
	}
	
	public <T> void testPersistentList(PersistentList<T> a) {
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
	}
}