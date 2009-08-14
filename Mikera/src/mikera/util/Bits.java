package mikera.util;

public class Bits {
	public static int countSetBits(int i) {
		i=(i&(0x55555555))+((i>>>1)&0x55555555);
		i=(i&(0x33333333))+((i>>>2)&0x33333333);
		i=((i+(i>>4))&0x0F0F0F0F);
		i=((i+(i>>8))&0x00FF00FF);
		i=((i+(i>>16))&0x0000FFFF);
		return i;
	}
	
	public static int roundUpToPowerOfTwo(int n) {
		n = n - 1;
		n = fillBitsRight(n);
		n = n + 1;
		return n;
	}
	
	public static long roundUpToPowerOfTwo(long n) {
		n = n - 1;
		n = fillBitsRight(n);
		n = n + 1;
		return n;
	}
	
	/** 
	 * Returns the number of bits required to fully represent the number (including sign)
	 * @param a
	 * @return
	 */
	public static int significantBits(long a) {
		if (a<0) return significantBits(-1-a);
		for (int i=0; i<64; i++) {
			long v=(1L<<i)-1;
			if (a<=v) return (i+1);
		}
		return 64;
	}
	
	public static int roundDownToPowerOfTwo(int n) {
		return n & (~(fillBitsRight(n)>>1));	
	}
	
	public static long roundDownToPowerOfTwo(long n) {
		return n & (~(fillBitsRight(n)>>1));
	}
	
	public static long fillBitsRight(long n) {
		n = n | (n >> 1);
		n = n | (n >> 2);
		n = n | (n >> 4);
		n = n | (n >> 8);
		n = n | (n >> 16);
		n = n | (n >> 32);
		return n;
	}
	
	public static int fillBitsRight(int n) {
		n = n | (n >> 1);
		n = n | (n >> 2);
		n = n | (n >> 4);
		n = n | (n >> 8);
		n = n | (n >> 16);
		return n;
	}
}
