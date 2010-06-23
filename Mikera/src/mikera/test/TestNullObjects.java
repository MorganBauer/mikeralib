package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.data.DataType;
import mikera.util.emptyobjects.*;
import java.util.*;

public class TestNullObjects {
	@SuppressWarnings("unchecked")
	@Test public void test1() {
		Map<String,Object> nm=(Map<String,Object>)NullMap.INSTANCE;
		
		assertEquals(null,nm.get("Hello"));
		
	}
	
	@Test public void testNullArrays() {
		for (DataType dt: DataType.values()) {
			Object test=NullArrays.getNullArray(dt);
			assertNotNull(test);
		}
	}
}
