package mikera.util;

import java.io.Serializable;
import java.util.Iterator;

import mikera.util.emptyobjects.NullArrays;

/**
 * Immutable char sequence implementation based on a tree with pre-computed hashcodes
 * 
 * Initially creates packed blocks, i.e. all blocks full except from final block, although this is not
 * guaranteed to be maintained (especially with concatenation / substring operations)
 * 
 * Also tries to maintain balanced tree
 * 
 * @author Mike
 *
 */
public final class Text implements CharSequence, Comparable<Text>, Iterable<Character>, Cloneable, Serializable {
	private static final long serialVersionUID = 5744895584967327995L;
	public static final int BLOCK_SIZE_BITS=6;
	public static final int BLOCK_SIZE=1<<BLOCK_SIZE_BITS;
	public static final Text EMPTY=new Text(NullArrays.NULL_CHARS);
	
	private final char[] data;
	private final Text front;
	private final Text back;
	private final int count;
	private final int hashCode;
	
	public static Text create(String s) {
		return create(s,0,s.length());
	}
	
	public static Text create(String s, int start, int end) {
		int length=end-start;
		if (length==0) return Text.EMPTY;
		if (length<=BLOCK_SIZE) {
			char[] chars=new char[length];
			s.getChars(start, end, chars, 0);
			return new Text(chars);
		} else {
			int mid=((start+end+(BLOCK_SIZE-1))>>(BLOCK_SIZE_BITS+1))<<(BLOCK_SIZE_BITS);
			return new Text(create(s,start, mid),create(s,mid, end));
		}
	}

	private Text(Text f, Text b) {
		data=null;
		front=f;
		back=b;
		count=f.count+b.count;
		hashCode=calculateConcatenatedHash(f,b);
	}

	
	private Text(char[] charData) {
		data=charData;
		count=data.length;
		back=null;
		front=null;
		hashCode=calculateHash(0,charData);
	}
	
	public Text subText(int start, int end) {
		if ((start<0)||(end>count)) throw new IndexOutOfBoundsException();
		if (start==end) return Text.EMPTY;
		if ((start==0)&&(end==count)) return this;
		if (data!=null) {
			int len=end-start;
			char[] ndata=new char[len];
			System.arraycopy(data, start, ndata, 0, len);
			return new Text(ndata);			
		} else {
			int frontCount=front.count;
			if (end<=frontCount) return front.subText(start,end);
			if (start>=frontCount) return back.subText(start-frontCount,end-frontCount);
			return concat(front.subText(start, frontCount),back.subText(0, end-frontCount));
		}
	}
	
	public int countNodes() {
		if (data!=null) {
			return 1;
		} else {
			return 1+front.countNodes()+back.countNodes();
		}
	}
	
	public int countBlocks() {
		if (data!=null) {
			return 1;
		} else {
			return front.countBlocks()+back.countBlocks();
		}
	}
	
	/**
	 * Deletes a block of text
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Text deleteRange(int start, int end) {
		if (start>=end) return this;
		if ((start<=0)&&(end>=count)) return Text.EMPTY;
		if (start<=0) return subText(end,count);
		if (end>=count) return subText(0,start);
		return concat(subText(0,start),subText(end,count));
	}
	
	/**
	 * Concatenates two Text objects, balancing the tree as far as possible
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Text concat(Text a, Text b) {
		int alen=a.length(); if (alen==0) return b;
		int blen=b.length(); if (blen==0) return a;
		
		if (alen+blen<BLOCK_SIZE) {
			char[] ndata=new char[alen+blen];
			a.getChars(0, alen, ndata, 0);
			b.getChars(0, blen, ndata, alen);
			return new Text(ndata);
		}
		
		if ((alen<(blen>>1))&&(b.data==null)) {
			return new Text(concat(a,b.front),b.back);
		} 
		
		if ((blen<(alen>>1))&&(b.data==null)) {
			return new Text(a.front,concat(a.back,b));	
		}
		
		return new Text(a,b);
	}
	
	public boolean isPacked() {
		return isFullyPacked(this,true);
	}
	
	public Text append(String s) {
		return concat(this,Text.create(s));
	}
	
	public Text concat(Text t) {
		return concat(this,t);
	}
	
	private static boolean isFullyPacked(Text t, boolean end) {
		if (t.data!=null) {
			return (end)||(t.data.length==BLOCK_SIZE);
		} else {
			return isFullyPacked(t.front,false)&&(isFullyPacked(t.back,true));
		}
	}
	
	public String substring(int start, int end) {
		char[] chars=new char[end-start];
		getChars(start,end,chars,0);
		return new String(chars);
	}
	
	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		if ((srcBegin<0)||(srcEnd>count)) throw new IndexOutOfBoundsException();
		if (srcEnd<=srcBegin) return;
		
		int pos=srcBegin;
		Text t=getBlock(pos);
		int tpos=getBlockStartPosition(pos);
		int tlen=t.length();
		
		int offset=dstBegin-srcBegin;
		while (pos<srcEnd) {
			dst[pos+offset]=t.data[pos-tpos];
			pos++;
			if (pos>=tpos+tlen) {
				t=getBlock(pos);
				tpos=getBlockStartPosition(pos);
				if(t!=null) tlen=t.length();	
			}
		}
	}
	
	/**
	 * Calculated hashcode based on rolled character values plus the length of the character array
	 * 
	 * @param initialHash
	 * @param data
	 * @return
	 */
	public static int calculateHash(int initialHash,char[] data) {
		int result=0;
		for (int i=0; i<data.length; i++) {
			result=Bits.rollLeft(result, 7) ^ ((int)data[i]);
		}
		return result+data.length;
	}
	
	public static int calculateConcatenatedHash(Text front,Text back) {
		int frontCount=front.count;
		int backCount=back.count;
		int hc=front.hashCode()-frontCount;
		hc=Bits.rollLeft(hc, 7*back.length());
		hc=hc^(back.hashCode()-backCount);
		return hc+frontCount+backCount;
	}

	
	public int hashCode() {
		return hashCode;
	}

	public char charAt(int index) {
		if (data!=null) {
			return data[index];
		} else {
			int fc=front.count;
			if (fc>index) {
				return front.charAt(index);
			} else {
				return back.charAt(index);
			}
		}
	}

	public int length() {
		return count;
	}
	
	public Text firstBlock() {
		Text t=this;
		while (t.data==null) {
			t=t.front;
		}
		return t;
	}
	
	public Text getBlock(int pos) {
		if ((pos<0)||(pos>=count)) return null;
		return getBlockLocal(this,pos);
	}
	
	public int getBlockStartPosition(int pos) {
		if ((pos<0)||(pos>count)) throw new IndexOutOfBoundsException();
		return getBlockStartPositionLocal(this,pos);
	}
	
	private static Text getBlockLocal(Text head, int pos) {
		while (head.data==null) {
			int frontCount=head.front.count;
			if (pos<frontCount) {
				head=head.front;
			} else {
				pos-=frontCount;
				head=head.back;
			}
		}
		return head;
	}
	
	private static int getBlockStartPositionLocal(Text head, int pos) {
		int result=0;
		while (head.data==null) {
			int frontCount=head.front.count;
			if (pos<frontCount) {
				head=head.front;
			} else {
				pos-=frontCount;
				result+=frontCount;
				head=head.back;
			}
		}
		return result;
	}

	public CharSequence subSequence(int start, int end) {
		return new TextUtils.SourceSubSequence(this, start, end);
	}
	
	public String toString() {
		return new String(substring(0,count));
	}
	
	public Text clone() {
		return this;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Text) {
			Text t=(Text)o;
			if (hashCode!=t.hashCode) return false;
			return compareTo(t)==0;
		}
		return false;
	}

	public int compareTo(Text t) {
		int pos=0;
		int s1=0;
		int s2=0;
		Text text1=this.getBlock(0);
		Text text2=t.getBlock(0);
		int len1=text1.length();
		int len2=text2.length();

		while (true) {
			if (text1==null) {
				return (text2==null)?0:-1;
			}
			if (text2==null) {
				return 1;
			}
					
			int c=text1.data[pos-s1]-text2.data[pos-s2];
			if (c!=0) return c;
			
			pos++; 
			if (pos-s1>=len1) {
				text1=this.getBlock(pos);
				if (text1!=null) len1=text1.length();
				s1=pos;
			}
			if (pos-s2>=len2) {
				text2=t.getBlock(pos);
				if (text2!=null) len2=text2.length();
				s2=pos;
			}
		}
	}

	private class TextIterator implements Iterator<Character> {
		private int pos=0;
		private Text block=getBlock(0);
		private int blockStart=0;
		
		public boolean hasNext() {
			return pos<count;
		}

		public Character next() {
			char c=block.data[pos-blockStart];
			pos++;
			if (pos>=blockStart+block.count) {
				block=getBlock(pos);
				blockStart=pos;
			}
			
			return Character.valueOf(c);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public Iterator<Character> iterator() {
		return new TextIterator();
	}
}