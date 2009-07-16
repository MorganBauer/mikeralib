package mikera.net;

import java.util.*;
import java.nio.*;

/**
 * Cache to enable recycling of NIO ByteBuffers
 * 
 * @author Mike
 *
 */
public final class BufferCache {
	public TreeMap<Integer,ByteBuffer> buffers=new TreeMap<Integer,ByteBuffer>();
	
	private final boolean direct;
	
	private BufferCache(boolean allocateDirect) {
		direct=allocateDirect;
	}
	
	public synchronized ByteBuffer getBuffer(int size) {
		SortedMap<Integer,ByteBuffer> sm=buffers.tailMap(size);
		if ((sm==null)||(sm.isEmpty())) {
			ByteBuffer newItem=create(size);	
			return newItem;
		}
		Integer k=sm.firstKey();
		ByteBuffer bb=sm.remove(k);
		if (bb==null) throw new Error("Expected ByteBuffer with entry length "+k+" not found!!");
		return bb;
	}
	
	public static void recycle(ByteBuffer bb) {
		if (bb==null) throw new Error("Null ByteBuffer!!");
		if (bb.isDirect()) {
			directInstance().recycleBuffer(bb);
		} else {
			indirectInstance().recycleBuffer(bb);
		}
	}
	
	public synchronized void recycleBuffer(ByteBuffer bb) {
		bb.clear();
		buffers.put(bb.capacity(),bb);
	}
	
	public synchronized ByteBuffer grow(ByteBuffer bb, int size) {
		ByteBuffer target=create(size);
		bb.flip();
		target.put(bb);
		recycle(bb);
		return target;
	}
	
	private ByteBuffer create(int size) {
		// TODO: Consider allocateDirect here?
		if (direct) {
			return ByteBuffer.allocateDirect(size);
		} else {
			return ByteBuffer.allocate(size);
		}	
	}
	
	private static final BufferCache indirectInstance=new BufferCache(false);
	
	private static final BufferCache directInstance=new BufferCache(true);

	
	public static BufferCache directInstance() {
		return directInstance;
	}
	
	public static BufferCache indirectInstance() {
		return indirectInstance;
	}
	
	public static BufferCache instance() {
		return directInstance;
	}
}
