package mikera.test;

import org.junit.*;
import static org.junit.Assert.*;
import mikera.util.emptyobjects.*;
import java.util.*;

public class TestNullObjects {
	@Test public void test1() {
		Map<String,Object> nm=NullStringMap.INSTANCE;
		
		assertEquals(null,nm.get("Hello"));
		
	}
	

}
