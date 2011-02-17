package mikera.test;

import static org.junit.Assert.assertEquals;
import mikera.engine.Hex;

import org.junit.Test;

public class TestHex {
	@Test public void testRoundUp() {
		for (int i=0; i<6 ; i++) {
			int dx=Hex.HEX_DX[i];
			int dy=Hex.HEX_DY[i];
			assertEquals(i,Hex.direction(dx, dy));
		}
	}

}
