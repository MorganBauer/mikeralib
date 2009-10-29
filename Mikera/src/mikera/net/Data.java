package mikera.net;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

import mikera.util.Maths;

/**
 * Class representing a chunk of data
 * 
 * @author Mike
 *
 */
public final class Data extends AbstractList<Byte> implements Cloneable, Serializable {

	private static final long serialVersionUID = 293989965333996558L;
	private static final int DEFAULT_DATA_SIZE=50;
	
	private byte[] data;
	private int size=0;
	
	public Data() {
		this(DEFAULT_DATA_SIZE);
	}
	
	/**
	 * Creates a Data object with the given initial capacity
	 * @param length
	 */
	public Data(int length) {
		data=new byte[length];
	}
	
	private Data(byte[] bytes) {
		data=bytes;
		size=bytes.length;
	}
	
	private class DataIterator implements Iterator<Byte> {
		private int pos=0;
		
		public boolean hasNext() {
			return pos<data.length;
		}

		public Byte next() {
			return data[pos++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public Iterator<Byte> iterator() {
		return new DataIterator();
	}
	
	/**
	 * Creates data object by bulk reading from a (flipped) bytebuffer
	 * 
	 * @param bb
	 * @return
	 */
	public static Data create(ByteBuffer bb) {
		int remaining=bb.remaining();
		Data nd=new Data(remaining);
		bb.get(nd.data, bb.position(), remaining);
		nd.size=remaining;
		return nd;
	}
	
	public static Data create(byte[] bytes) {
		Data nd=new Data(bytes.length);
		nd.append(bytes, 0, bytes.length);
		return nd;
	}
	
	public static Data wrap(byte[] bytes) {
		return new Data(bytes);
	}
	
	public void append(byte b) {
		put(size,b);
	}
	
	public void appendInt(int v) {
		int pos=size;
		ensureCapacity(size+4);
		data[pos]=(byte)(v);
		data[pos+1]=(byte)(v>>>8);
		data[pos+2]=(byte)(v>>>16);
		data[pos+3]=(byte)(v>>>24);
		size+=4;
	}
	
	public void appendFloat(float v) {
		appendInt(Float.floatToIntBits(v));
	}
	
	public void appendLong(long lv) {
		appendInt((int)(lv));
		appendInt((int)(lv>>32));
	}
	
	public void append(Data d) {
		put(size,d,0,d.size());
	}
	
	public void append(byte[] bs, int offset, int len) {
		put(size,bs,offset,len);
	}
	
	public void put(int pos, byte b) {
		if (pos+1>size) {
			ensureCapacity(pos+1);
			size=pos+1;
		}		
		data[pos]=b;
	}
	
	public Byte set(int pos, Byte b) {
		Byte result=get(pos);
		put(pos,b);
		return result;
	}
	
	public void put(int pos, byte[] bs, int offset, int len) {
		if (pos+len>size) {
			ensureCapacity(pos+len);
			size=pos+len;
		}
		System.arraycopy(bs, offset, data, pos, len);
	}
	
	public void put(int pos, Data d, int offset, int len) {
		if (pos+len>size) {
			ensureCapacity(pos+len);
			size=pos+len;
		}
		System.arraycopy(d.data, offset, data, pos, len);
	}
	
	public void copy(int pos, byte[] dest, int destoffset, int len) {
		System.arraycopy(data, pos, dest, destoffset, len);	
	}
	
	public void copy(int pos, Data dest, int destoffset, int len) {
		dest.put(destoffset,data,pos,len);	
	}
	
	public int size() {
		return size;
	}
	
	public void clear() {
		size=0;
	}
	
	public byte[] getInternalData() {
		return data;
	}
	
	public void ensureCapacity(int len) {
		if (data.length<len) {
			int nlen=Maths.max(len,data.length*2);
			byte[] ndata=new byte[nlen];
			System.arraycopy(data, 0, ndata,0, size);
			data=ndata;
		}
	}
	
	public ByteBuffer toFlippedByteBuffer() {
		ByteBuffer bb=BufferCache.instance().getBuffer(size);
		bb.put(data,0,size);
		bb.flip();
		return bb;
	}
	
	public ByteBuffer toWrapByteBuffer() {
		ByteBuffer bb=ByteBuffer.wrap(data);
		bb.limit(size);
		return bb;
	}
	
	public byte[] toNewByteArray() {
		byte[] bs=new byte[size];
		System.arraycopy(data, 0, bs,0, size);
		return bs;
	}
	
	public int hashCode() {
		int result=0;
		for(int i=0; i<size; i++) {
			result^=data[i];
			result=Integer.rotateRight(result, 1);
		}
		return result;
	}
	
	public Data clone() {
		Data nd=new Data(size);
		copy(0,nd,0,size);
		return nd;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o instanceof List<?>) {
			return equals((List<Byte>) o);
		}
		return super.equals(o);
	}
	
	public boolean equals(List<Byte> l) {
		int size=size();
		if (size!=l.size()) return false;
		for (int i=0; i<size; i++) {
			if (data[i]!=l.get(i)) return false;
		}
		return true;
	}
	
	public byte getByte(int pos) {
		if ((pos<0)||(pos>=size)) throw new IndexOutOfBoundsException();
		return data[pos];
	}
	
	public int getInt(int pos) {
		if ((pos<0)||((pos+3)>=size)) throw new IndexOutOfBoundsException();
		return ((int)data[pos]&255)
	      |(((int)data[pos+1]&255)<<8)		
	      |(((int)data[pos+2]&255)<<16)		
	      |(((int)data[pos+3]&255)<<24);		
	}
	
	public float getFloat(int pos) {
		return Float.intBitsToFloat(getInt(pos));
	}
	
	public long getLong(int pos) {
		long lv=((long)getInt(pos))&0xFFFFFFFFl;
		lv^=((long)getInt(pos+4))<<32;
		return lv;
	}

	public Byte get(int pos) {
		return getByte(pos);
	}
}
