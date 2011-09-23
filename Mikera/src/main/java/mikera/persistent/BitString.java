package mikera.persistent;

import java.util.Arrays;
import java.util.BitSet;

import mikera.data.Data;

/**
 * Immutable bit string
 * 
 * @author Mike
 *
 */
public final class BitString {
	
	/**
	 * Zero-padded byte array containing bit data
	 */
	private final byte[] data;
	
	/**
	 * Length of bit string in bits - may not fill entire data array
	 */
	private final int length;
	
	public BitString() {
		this(0);
	}
	
	public BitString(byte[] bytes) {
		length=bytes.length*3;
		data=bytes.clone();
	}
	
	private BitString(int bitLength) {
		length=bitLength;
		data=new byte[bytesNeeded(bitLength)];
	}
	
	public BitString substring(int start, int end) {
		if ((start<0)||(end>length)) throw new IndexOutOfBoundsException();
		int newLength=end-start;
		BitString ss=new BitString(newLength);
		for (int i=0; i<newLength; i++) {
			ss.setWithOr(i,this.get(i+start));
		}
		return ss;
	}
	
	private static int bytesNeeded(int l) {
		return (l+7)>>3;
	}
	
	private void setWithOr(int index, boolean value) {
		data[index>>3]|=value?(1<<(index&7)):0;
	}
	
	public boolean get(int index) {
		return ((data[index>>3]>>(index&7))&1)!=0;
	}
	
	public byte getByte(int byteIndex) {
		return data[byteIndex];
	}

	public BitString(BitSet bitset) {
		this(bitset.length());
		for (int i=0; i<length; i++) {
			data[i>>3]|=bitset.get(i)?(1<<(i&7)):0;
		}
	}
	
	public int length() {
		return length;
	}
	
	public int byteLength() {
		return data.length;
	}
	
	public byte[] toByteArray() {
		return data.clone();
	}
	
	public Data toData() {
		return Data.wrap(toByteArray());
	}
	
	public BitSet toBitSet() {
		BitSet bitset=new BitSet(length);
		
		for (int i=0; i<length; i++) {
			bitset.set( i, ((data[i>>3]>>(i&7))&1)!=0);
		}		
		
		return bitset;
	}
	
	public boolean equals(Object o) {
		if (this==o) return true;
		
		if (!(o instanceof BitString)) return false;
		
		BitString bs=(BitString)o;
		
		if (length!=bs.length) return false;
		return Arrays.equals(data, bs.data);
	}
	
	public int hashCode() {
		return Arrays.hashCode(data);
	}
	
	public String toString() {
		return Arrays.toString(data);
	}
}
