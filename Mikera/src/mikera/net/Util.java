package mikera.net;

import java.nio.*;

public class Util {
	public static int writeCompacted(ByteBuffer bb, long a) {
		byte b=(byte)(a&127); // get bottom 7 bits
		a>>=7;
		int i=0;
		while ((((b&64)==0)&&(a!=0))||(((b&64)!=0)&&(a!=-1))) {
			b|=128;
			bb.put(b); i++;
			b=(byte)(a&127);
			a>>=7;
		}
		bb.put(b); i++;
		return i;
	}
	
	
	public static long readCompacted(ByteBuffer bb) {
		long result=0;
		long b=0;
		int bits=0;
		do {
			b=bb.get();
			result|=(b&127)<<bits;
			bits+=7;
		} while ((b&128)!=0);
		// if ((bits<64)&&((result&(1L<<(bits-1)))!=0)) {
		if ((bits<64)) {
			// sign extend
			result=(result<<(64-bits))>>(64-bits);
		}
		return result;
	}
}
