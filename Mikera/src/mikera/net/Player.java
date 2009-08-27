/**
 * 
 */
package mikera.net;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Player {
	String name=null;
	String password=null;
	Connection connection=null;
	List<ByteBuffer> messages=Collections.synchronizedList(new LinkedList<ByteBuffer>());
	Integer id=null;
}