package mikera.net;

import java.nio.*;
import java.nio.charset.*;

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
	
	public static int compactedLength(long a) {
		byte b=(byte)(a&127); // get bottom 7 bits
		a>>=7;
		int i=0;
		while ((((b&64)==0)&&(a!=0))||(((b&64)!=0)&&(a!=-1))) {
			b|=128;
			i++;
			b=(byte)(a&127);
			a>>=7;
		}
		i++;
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
	
	

	public static int writeASCIIString(ByteBuffer bb, String src) {

		int startPos=bb.position();
		
		if (src!=null) {		
			int len=src.length();
			writeCompacted(bb,len);			
			for (int i=0; i<len; i++) {
				bb.put((byte)(src.charAt(i)));
			}
		} else {
			// send -1 for null string
			writeCompacted(bb,-1);	
		}
		return bb.position()-startPos;
	}
	
	public static int readASCIIString(ByteBuffer bb, String[] dest) {
		int startPos=bb.position();
		int len=(int)readCompacted(bb);

		dest[0]=readASCIIStringChars(bb,len);
		
		return bb.position()-startPos;
	}
	
	public static String readASCIIString(ByteBuffer bb) {
		int len=(int)readCompacted(bb);
		return readASCIIStringChars(bb,len);
	}
	
	public static String readASCIIStringChars(ByteBuffer bb, int numChars) {
		int len=numChars;
		if (len<0) return null;
		char[] readChars=new char[len];
		for (int i=0; i<len; i++) {
			readChars[i]=(char)(bb.get());
		}
		return new String(readChars);
	}
}
