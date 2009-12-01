package mikera.net;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

import mikera.util.Maths;
import mikera.util.TextUtils;
import mikera.util.emptyobjects.NullArrays;

/**
 * Class representing a chunk of data as a mutable
 * variable length block of bytes
 * 
 * Note: big-endian format used for numbers, this allows for 
 * data comparison to be equivalent to numerical comparison
 * 
 * @author Mike
 *
 */
public final class Data extends AbstractList<Byte> implements Cloneable, Serializable, Comparable<Data>, Externalizable {
	private static final long serialVersionUID = 293989965333996558L;
	private static final int DEFAULT_DATA_INCREMENT=50;
	
	private byte[] data=NullArrays.NULL_BYTES;
	private int size=0;
	
	public Data() {
	}
	
	/**
	 * Creates a Data object with the given initial capacity
	 * @param length
	 */
	public Data(int length) {
		data=new byte[length];
	}
	
	public Data(Data d) {
		this(d.size());
		d.copyTo(0, this, 0, d.size());
	}
	
	public Data(ByteBuffer bb) {
		appendByteBuffer(bb);
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
		bb.get(nd.data, 0, remaining);
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
	
	public byte getByte(int pos) {
		if ((pos<0)||(pos>=size)) throw new IndexOutOfBoundsException();
		return data[pos];
	}
	
	public boolean getBoolean(int pos) {
		if ((pos<0)||(pos>=size)) throw new IndexOutOfBoundsException();
		return data[pos]!=0;
	}
	
	public int getInt(int pos) {
		if ((pos<0)||((pos+3)>=size)) throw new IndexOutOfBoundsException();
		return ((int)data[pos+3]&255)
	      |(((int)data[pos+2]&255)<<8)		
	      |(((int)data[pos+1]&255)<<16)		
	      |(((int)data[pos]&255)<<24);		
	}
	
	public char getChar(int pos) {
		if ((pos<0)||((pos+1)>=size)) throw new IndexOutOfBoundsException();
		int res= ((data[pos+1])&(255))
	      |(((data[pos])&(255))<<8);
		return (char)res;
	}
	
	public short getShort(int pos) {
		if ((pos<0)||((pos+1)>=size)) throw new IndexOutOfBoundsException();
		int res= ((data[pos+1])&(255))
	      |(((data[pos])&(255))<<8);
		return (short)res;
	}
	
	public float getFloat(int pos) {
		return Float.intBitsToFloat(getInt(pos));
	}
	
	public long getLong(int pos) {
		long lv=((long)getInt(pos+4))&0xFFFFFFFFl;
		lv^=((long)getInt(pos))<<32;
		return lv;
	}
	
	public double getDouble(int pos) {
		return Double.longBitsToDouble(getLong(pos));
	}

	public Byte get(int pos) {
		return getByte(pos);
	}
	
	public void appendByte(byte b) {
		put(size,b);
	}
	
	public void appendBoolean(boolean b) {
		put(size,(byte)(b?1:0));
	}
	
	public void appendInt(int v) {
		int pos=size;
		ensureCapacity(size+4);
		data[pos+3]=(byte)(v);
		data[pos+2]=(byte)(v>>>8);
		data[pos+1]=(byte)(v>>>16);
		data[pos]=(byte)(v>>>24);
		size+=4;
	}
	
	public void appendByteBuffer(ByteBuffer bb) {
		int rem=bb.remaining();
		ensureCapacity(size+rem);
		bb.get(data, size, rem);
		size+=rem;
	}
	
	public void appendChar(char v) {
		int pos=size;
		ensureCapacity(size+2);
		data[pos+1]=(byte)(v);
		data[pos]=(byte)(v>>>8);
		size+=2;
	}
	
	public void appendShort(short v) {
		int pos=size;
		ensureCapacity(size+2);
		data[pos+1]=(byte)(v);
		data[pos]=(byte)(v>>>8);
		size+=2;
	}
	
	public void appendFloat(float v) {
		appendInt(Float.floatToIntBits(v));
	}
	
	public void appendDouble(double v) {
		appendLong(Double.doubleToLongBits(v));
	}
	
	public void appendLong(long lv) {
		appendInt((int)(lv>>32));
		appendInt((int)(lv));
	}
	
	public void appendString(String s) {
		int len=s.length();
		appendInt(len);
		for (int i=0; i<len; i++) {
			appendChar(s.charAt(i));
		}
	}
	
	public String getString(int pos) {
		int len=getInt(pos);
		pos+=4;
		char[] cs=new char[len];
		for (int i=0; i<len; i++) {
			cs[i]=getChar(pos+i*2);
		}
		return new String(cs);	
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
	
	public void copyTo(int pos, byte[] dest, int destoffset, int len) {
		System.arraycopy(data, pos, dest, destoffset, len);	
	}
	
	public void copyTo(int pos, Data dest, int destoffset, int len) {
		dest.put(destoffset,data,pos,len);	
	}
	
	public Data subset(int start, int end) {
		if ((start<0)||(end>size)) throw new IllegalArgumentException();
		int len=end-start;
		Data d=new Data(len);
		copyTo(start,d,0,len);
		return d;
	}
	
	public int size() {
		return size;
	}
	
	public void clear() {
		size=0;
		data=NullArrays.NULL_BYTES;
	}
	
	byte[] getInternalData() {
		return data;
	}
	
	public void ensureCapacity(int len) {
		int dlen=data.length;
		
		// extend data array if too small
		if (dlen<len) {
			int nlen=Maths.max(len,dlen*2,dlen+DEFAULT_DATA_INCREMENT);
			byte[] ndata=new byte[nlen];
			System.arraycopy(data, 0, ndata,0, size);
			data=ndata;
		}
	}
	
	public int currentCapacity() {
		return data.length;
	}
	
	public void writeToByteBuffer(ByteBuffer bb) {
		bb.put(data,0,size);
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
	
	public ByteBuffer wrapAndClear() {
		ByteBuffer bb=ByteBuffer.wrap(data);
		bb.limit(size);
		clear();
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
	
	@Override
	public Data clone() {
		Data nd=new Data(size);
		copyTo(0,nd,0,size);
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
	
	public int compareTo(Data d) {
		int n=Maths.min(size(), d.size());
		for (int i=0; i<n; i++) {
			int bd=getByte(i)-d.getByte(i);
			if (bd!=0) return bd;
		}
		if (size()<d.size()) return -1;
		if (size()>d.size()) return 1;
		return 0;
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		int s=size();
		for (int i=0; i<s; i++) {
			if (i>0) {
				if ((i&15)==0) {
					sb.append('\n');
				} else {
					sb.append(' ');					
				}
			}
			int b=data[i];
			sb.append(TextUtils.toHexChar(b>>4));
			sb.append(TextUtils.toHexChar(b));
		}
		
		return sb.toString();
	}

	public void readExternal(ObjectInput oi) throws IOException,
			ClassNotFoundException {
		int len=oi.readInt();
		data=new byte[len];
		int res=oi.read(data, 0, len);
		if (res!=len) throw new IOException("Error: "+res+" bytes read out of length "+len+" expected");
		size=len;
	}

	public void writeExternal(ObjectOutput oo) throws IOException {
		oo.writeInt(size);
		oo.write(data, 0, size);
	}

	public static int sizeOfBoolean(boolean b) {
		return 1;
	}
	
	public static int sizeOfByte(byte b) {
		return 1;
	}
	
	public static int sizeOfShort(short b) {
		return 2;
	}
	
	public static int sizeOfChar(char b) {
		return 2;
	}
	
	public static int sizeOfInt(int b) {
		return 4;
	}
	
	public static int sizeOfFloat(float b) {
		return 4;
	}
	
	public static int sizeOfDouble(double b) {
		return 8;
	}
	
	public static int sizeOfLong(long b) {
		return 8;
	}
	
	public static int sizeOfString(String s) {
		return 4+2*s.length();
	}
	
}
