package mikera.util;

public final class Bits {
	
	public static int countSetBits(int i) {
		i=(i&(0x55555555))+((i>>>1)&0x55555555);
		i=(i&(0x33333333))+((i>>>2)&0x33333333);
		i=((i+(i>>4))&0x0F0F0F0F);
		i=((i+(i>>8)));
		i=((i+(i>>16)));
		return i&0x3F;
	}
	
	// alternative count set bits operation
	// currently seems slightly slower
	public static int countSetBits2(int i) {
		return Integer.bitCount(i);
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
	
	public static int roundDownToPowerOfTwo(int n) {
		return n & (~(fillBitsRight(n>>>1)));	
	}
	
	public static long roundDownToPowerOfTwo(long n) {
		return n & (~(fillBitsRight(n>>>1)));
	}
	
	
	/** 
	 * Returns the number of bits required to fully represent the signed number (including sign)
	 * @param a
	 * @return
	 */
	public static int significantSignedBits(long a) {
		if (a<0) a=-a-1;
		for (int i=0; i<64; i++) {
			long v=(1L<<i)-1;
			if (a<=v) return (i+1);
		}
		return 64;
	}
	
	/** 
	 * Returns the number of bits required to fully represent the unsigned number 
	 * @param a
	 * @return
	 */
	public static int significantUnsignedBits(int a) {
		return 32-countLeadingZeros(a);
	}
	
	public static boolean isUnsignedPowerOfTwo(long a) {
		return (a&(a-1))==0;
	}
	
	public static int countTrailingZeros(long a) {
		int al=(int)a;
		if (al==0) return (32+countTrailingZeros((int)(a>>>32)));
		return countTrailingZeros(al);
	}
	
	public static int countTrailingZeros(int a) {
		int r=0;
		if ((a&0xFFFF)==0) {r+=16; a>>>=16;}
		if ((a&0xFF)==0) {r+=8; a>>>=8;}
		if ((a&0xF)==0) {r+=4; a>>>=4;}
		if ((a&0x3)==0) {r+=2; a>>>=2;}
		if ((a&0x1)==0) {r+=1; a>>>=1;}
		if ((a&0x1)==0) {r+=1; }
		return r;
	}
	
	public static int countLeadingZeros(long a) {
		int ah=(int)(a>>>32);
		if (ah==0) return (32+countLeadingZeros((int)(a)));
		return countLeadingZeros(ah);
	}
	
	public static int countLeadingZeros(int a) {
		int r=0;
		if ((a&0xFFFF0000)==0) {r+=16;} else {a>>>=16;}
		if ((a&0xFF00)==0) {r+=8;} else {a>>>=8;}
		if ((a&0xF0)==0) {r+=4;} else {a>>>=4;}
		if ((a&0xC)==0) {r+=2;} else {a>>>=2;}
		if ((a&0x2)==0) {r+=1;} else {a>>>=1;}
		if ((a&0x1)==0) {r+=1;}
		return r;
	}
	
	public static int countLeadingZeros2(int a) {
		return 32-Integer.bitCount(fillBitsRight(a));
	}
	
	public static int signExtend(int a, int bits) {
		int shift=32-bits;
		return (a<<shift)>>shift;
	}

	
	public static int lowestSetBit(int a) {
		return (a & (-a));
	}
	
	public static int lowestSetBitIndex(int a) {
		return countTrailingZeros(a);
	}
	
	public static int highestSetBit(int a) {
		return a&(~fillBitsRight(a>>>1));
	}
	
	public static int highestSetBitIndex(int a) {
		return 31-countLeadingZeros(a);
	}
	
	/**
	 * Gets the n'th set bit 
	 * 
	 * @param a integer containing bits to search
	 * @param bitno position of bit from 1 to 32
	 * @return integer containing just the n'th set bit
	 */
	public static int getNthSetBit(int a, int bitno) {
		if (bitno<=0) return 0;
		for (int i=1; i<bitno; i++) {
			a^=(a & (-a));
		}
		return (a&(-a));
	}
	
	/**
	 * Gets the position (index) of the n'th set bit 
	 * 
	 * @param a integer containing bits to search
	 * @param bitno position of bit from 1 to 32
	 * @return integer containing just the n'th set bit
	 */
	public static int getNthSetBitIndex(int a, int bitno) {
		if (bitno<=0) return 0;
		for (int i=1; i<bitno; i++) {
			a^=(a & (-a));
		}
		return Bits.countTrailingZeros(a);
	}
	
	public static int reverseBits(int a) {
		a = ((a >>> 1)  & 0x55555555) | ((a << 1)  & 0xAAAAAAAA);
		a = ((a >>> 2)  & 0x33333333) | ((a << 2)  & 0xCCCCCCCC);
		a = ((a >>> 4)  & 0x0F0F0F0F) | ((a << 4)  & 0xF0F0F0F0);
		a = ((a >>> 8)  & 0x00FF00FF) | ((a << 8)  & 0xFF00FF00);
		a = ((a >>> 16) & 0x0000FFFF) | ((a << 16) & 0xFFFF0000);	
		return a;
	}
	
	public static long reverseBits(long a) {	
		return (((0xFFFFFFFFL&reverseBits((int)a)))<<32)^(0xFFFFFFFFL&reverseBits((int)(a>>>32)));
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
	
	public static int rollRight(int v, int count) {
		return Integer.rotateRight(v, count);
		//count&=31;
		//v=(v>>>count)|(v<<(32-count));
		//return v;
	}
	
	public static int rollLeft(int v, int count) {
		return Integer.rotateLeft(v, count);
		//count&=31;
		//v=(v<<count)|(v>>>(32-count));
		//return v;
	}
	
	public int parity(int v) {
		v ^= v >> 16;
		v ^= v >> 8;
		v ^= v >> 4;
		v &= 0xf;
		return (0x6996 >> v) & 1;
	}

	public static long zigzagDecodeLong(final long n) {
		return (n >>> 1) ^ -(n & 1);
	}

	public static int zigzagDecodeInt(final int n) {
	return (n >>> 1) ^ -(n & 1);
	}

	public static long zigzagEncodeLong(final long n) {
		return (n << 1) ^ (n >> 63);
	}

	public static int zigzagEncodeInt(final int n) {
		return (n << 1) ^ (n >> 31);
	}
	
	public static int nextIntWithSameBitCount(int a) {
		int lowest = a & -a;
		int t=a+lowest; 
		
		if (t==0) return (a >>> (lowestSetBitIndex(lowest))); // overflow case
		
		int changed = (a ^ t); // changed bits from addition, i.e. lost 1s plus one new 1, starting just above lowest
		int lostOnes= (changed >>> (2+lowestSetBitIndex(lowest))); // lost ones to be added back as least significant bits
		return lostOnes | t ;
	}
	
	public static String toBinaryString(int a) {
		StringBuilder sb=new StringBuilder();
		for (int i=31; i>=0; i--) {
			sb.append((((a>>i)&1)!=0)?'1':'0');
		}
		return sb.toString();
	}
}
