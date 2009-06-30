package mikera.engine;

import mikera.util.Rand;
import java.util.*;


public class Octreap<T> implements Cloneable {
	private static final int BITS=20;
	private static final int BITS_POWER2=1<<BITS;
	private static final int BITS_MASK=BITS_POWER2-1;
	private static final long FULL_MASK=(BITS_MASK)*(1L+((1L+(1L<<BITS))<<BITS));
	
	public static interface NodeVisitor {
		public Object visit(ZNode n);
	}
	
	public static final class ZNode implements Comparable<ZNode>, Cloneable {
		private long z1;
		private long z2;
		private ZNode left;
		private ZNode right;
		private Object object;
		private int priority;
		
		public ZNode() {
			priority=Rand.nextInt();
		}
		
		public int compareTo(ZNode b) {
			long a=z1-b.z1;
			if (a>0) return 1;
			if (a<0) return -1;
			return 0;
		}
		
		public Object clone() {
			try {
				ZNode zn=(ZNode)super.clone();
				if (zn.left!=null) zn.left=(ZNode)(zn.left.clone());
				if (zn.right!=null) zn.right=(ZNode)(zn.right.clone());
				return zn;
			} catch (Throwable t) {
				throw new Error(t);
			}
		}
		
		public boolean equals(ZNode a) {
			// note we ignore priority - not content relevant
			if (this==a) return true;
			if (!object.equals(a.object)) return false;
			if (z1!=a.z1) return false;
			if (z2!=a.z2) return false;
			return true;
		}
	}
	
	public ZNode head;
	
	public void set(int x, int y, int z, T value) {
		long zz=calculateZ(x,y,z);
		setRange(zz,zz,value);
	}
	
	public void clear() {
		head=null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object b) {
		if (!(b instanceof Octreap)) return false;
		if (this==b) return true;
		Octreap<T> o=(Octreap<T>) b;
		ZNode an=this.getFirstNode();
		ZNode bn=o.getFirstNode();
		while((an!=null)||(bn!=null)) {
			if ((an==null)^(bn==null)) return false;
			if (!an.equals(bn)) return false;
			an=this.nextNode(an.z1);
			bn=o.nextNode(bn.z1);
		}
		return true;
	}
	

	private class NodeIterator implements Iterator<ZNode> {
		private ZNode current=getFirstNode();
		
		public boolean hasNext() {
			return (current!=null);
		}

		public ZNode next() {
			ZNode result=current;
			if (result!=null) {
				ZNode next=nextNode(result.z1);
				current=next;
			}
			return result;
		}

		public void remove() {
			throw new Error("Not supported");
		}
	}
	
	public ZNode nextNode(long zz) {
		ZNode next=null;
		ZNode ze=head;
		while (ze!=null) {
			if (ze.z1>zz) {
				if ((next==null)||(ze.z1<next.z1)) next=ze;
				ze=ze.left;
			} else {
				ze=ze.right;
			}
		}
		return next;
	}
	
	public Iterator<ZNode> getNodeIterator() {
		NodeIterator ni=new NodeIterator();
		return ni;
	}
	
	public void visitBlocks(BlockVisitor<T> bf) {
		visitBlocks(head,bf);
	}
	
	@SuppressWarnings("unchecked")
	public void visitBlocks(ZNode node, BlockVisitor<T> bf) {
		if (node==null) return;
		visitBlocks(node.left,bf);
		
		long pos=node.z1;
		while (pos<=node.z2) {
			long size=blockSize(pos,node);
			
			long pos2=pos+size-1;
			bf.visit(extractX(pos), extractY(pos), extractZ(pos), extractX(pos2), extractY(pos2), extractZ(pos2), (T)node.object);
			pos+=size;
		}
		
		visitBlocks(node.right,bf);
	}
	
	/*
	 * Gets the largest power of two sided block fitting in node and starting at pos
	 */
	protected static final long blockSize(long pos, ZNode node) {
		long size=1;
		while (((pos&(size-1))==0)&&((pos+size-1)<=node.z2)) {
			size=size<<1;
		}		
		return size;
	}
	
	protected static final long blockRoot(long pos, ZNode node) {
		long size=1;
		while (size<FULL_MASK) {
			if ((pos&size)>0) {
				if ((pos-size)<node.z1) return pos;
				pos=pos-size;
			} else {
				if ((pos+size)>node.z2) return pos;
			}
			size<<=1;
		}		
		return pos;
	}
	
	
	public void visitNodes(NodeVisitor nf) {
		visitNodes(head,nf);
	}
	
    private boolean checkEquals(T a, T b) {
    	if (a==b) return true;
    	if (a==null) {
    		return (b==null);
    	} else {
    		return a.equals(b);
    	}
    }
	
	@SuppressWarnings("unchecked")
	public void delete(Octreap t) {
		NodeVisitor deleter=new NodeVisitor() {
			public Object visit(ZNode n) {
				deleteRange(n.z1,n.z2);
				return null;
			}
		};
		t.visitNodes(deleter);
	}
	
	
	public void floodFill(int x, int y, int z, T value) {
		T fromValue=get(x,y,z);
		if (checkEquals(value,fromValue)) return;
		floodFill(x,y,z,value,fromValue);
	}
	
	public void floodFill(int x, int y, int z, T value, T fromValue) {
		throw new Error("Not yet supported");
	}
	
	public void mergeFrom(Octreap<T> t) {
		NodeVisitor merger=new NodeVisitor() {
			@SuppressWarnings("unchecked")
			public Object visit(ZNode n) {
				setRange(n.z1,n.z2,(T)n.object);
				return null;
			}
		};
		t.visitNodes(merger);
	}
	
	@SuppressWarnings("unchecked")
	public Octreap<T> clone() {
		try {
			Octreap<T> zn=(Octreap<T>)super.clone();
			zn.head=(ZNode)(zn.head.clone());
			return zn;
		} catch (Throwable t) {
			throw new Error(t);
		}
	}
	
	private void visitNodes(ZNode node, NodeVisitor nf) {
		if (node==null) return;
		visitNodes(node.left,nf);
		nf.visit(node);
		visitNodes(node.right,nf);
	}
	
	public void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value) {
		if (x1>(x2)) {int temp=x1; x1=x2; x2=temp;}
		if (y1>(y2)) {int temp=y1; y1=y2; y2=temp;}
		if (z1>(z2)) {int temp=z1; z1=z2; z2=temp;}
		setBlockLocal(x1,y1,z1,x2,y2,z2,value);
	}
	
	private void setBlockLocal(int x1, int y1, int z1, int x2, int y2, int z2, T value) {
		long zz1=calculateZ(x1,y1,z1);
		long zz2=calculateZ(x2,y2,z2);
		long mask=FULL_MASK;
		setBlock(zz1,zz2,value,mask);
	}
	
	public void setBlock(long zz1, long zz2, T value, long mask) {
		if (zz1==zz2) {
			setRange(zz1,zz2,value);
			return;
		}
			
		// find next difference
		long hm=mask>>1;
		long hb=mask-hm;
		while ((zz1&hb)==(zz2&hb)) {
			// escape if at single point or adjacent points
			if (mask<=3) {
				setRange(zz1,zz2,value);
				return;
			}
			
			mask=hm;
			hm=mask>>1;
			hb=mask-hm;
		}
		
		// check if single block
		if (((zz1&mask)==0)&&((zz2&mask)==mask)) {
			setRange(zz1,zz2,value);
			return;
		}
		
		//if ((mask&(mask+1))!=0) throw new Error("Mask issue!!");
		long highbits=zz1&(~mask);
		//if (highbits!=(zz1&(~mask))) throw new Error("Diferent high bits issue!!");
		
		long dmask=fillBits3(hb>>3); // lower bits of dimension being split
		long omask=hm-dmask; // other bits
		setBlock(zz1,highbits|(zz1&hb)|dmask|(zz2&omask),value,hm);
		setBlock(highbits|(zz2&hb)|(zz1&omask),zz2,value,hm);
		
	}
	
	public static long fillBits3(long m) {
		m=m|(m>>3);
		m=m|(m>>6);
		m=m|(m>>12);
		m=m|(m>>24);
		m=m|(m>>48);
		return m;
	}

	
	@SuppressWarnings("unchecked")
	public final T get(long zz) {
		ZNode ze=getNode(zz);
		if (ze!=null) return (T)ze.object;
		return null;
	}
	
	private final ZNode getNode(long zz) {
		ZNode ze=head;
		while (ze!=null) {
			if (zz<ze.z1) {
				ze=ze.left;
				continue;
			}
			if (zz>ze.z2) {
				ze=ze.right;
				continue;
			}
			return ze;
		}
		return null;		
	}
	
	private final ZNode getFirstNode() {
		ZNode ze=head;
		while (ze!=null) {
			if (ze.left==null) return ze;
			ze=ze.left;
		}
		return null;			
	}
	
	public final T get(int x, int y, int z) {
		long zz=calculateZ(x,y,z);
		return get(zz);
	}
	
	protected ZNode getParentNode(ZNode node) {
		long zz=node.z1;
		ZNode ze=head;
		if (ze==node) return null;
		while (ze!=null) {
			if (zz<ze.z1) {
				if (ze.left==node) return ze;
				ze=ze.left;
				continue;
			}
			if (zz>ze.z2) {
				if (ze.right==node) return ze;
				ze=ze.right;
				continue;
			}
			throw new Error("Node not found!");	
		}
		throw new Error("Node not found!");	
	}
	
	/**
	 * Checks for consistency of ZNode tree
	 */
	public boolean check() {
		return check(head);
	}

	private boolean check(ZNode node) {
		if (node==null) return true;
		if (node.z1>node.z2) throw new Error("Inverted node");
		if (node.object==null) throw new Error("Null object");
		
		if (node.left!=null) {
			if (node.priority<node.left.priority) throw new Error("Priority problem");
			if (node.z1<=node.left.z2) throw new Error("Bounds problem");
			if (!check(node.left)) return false;
		}
		
		if (node.right!=null) {
			if (node.priority<node.right.priority) throw new Error("Priority problem");
			if (node.z2>=node.right.z1) throw new Error("Bounds problem");
			if (!check(node.right)) return false;
		}
		
		return true;
	}
	
	/**
	 * Checks whether entire range is empty (null)
	 */
	public boolean isEmpty(long za, long zb) {
		return isEmpty(za, zb, head);
	}
	
	public boolean isEmpty(long za, long zb, ZNode node) {
		while (node!=null) {
			if ((za<node.z2)&&(zb>=node.z1)) return false;
			node=(zb<node.z1)?node.left:node.right;
		}
		return true;
	}
	
	public void setRange(long za, long zb, T value) {
		//if (za>zb) {
		//	throw new Error("Setrange inverted");
		//}
		
		// special null case
		if (value==null) {
			deleteRange(za,zb);
			return;
		}
		
		// try to do a quick change
		ZNode node=head;
		while (node!=null) {
			if (zb<node.z1) {
				node=node.left;
				continue;
			}
			if (za>node.z2) {
				node=node.right;
				continue;
			}
			// we have overlap and/or adjacency!
			
			// check for complete overwrite case		
			if ((za<=node.z1)&&(zb>=node.z2)) {
				boolean setLow=false;
				boolean setHigh=false;
				if (za<node.z1) setLow=true;
				if (zb>node.z2) setHigh=true;
				
				boolean matchValue=node.object.equals(value);
				if (!matchValue) node.object=value;
				
				if (setLow) {
					deleteRange(za,node.z1-1);
					node.z1=za; 
				}
				if (setHigh) {
					deleteRange(node.z2+1,zb);		
					node.z2=zb; 
				}
				if (setLow||(!matchValue)) tryMerge(za);
				if (setHigh||(!matchValue)) tryMerge(zb+1);
				return;
			}
			
			// check for extend case
			if (node.object.equals(value)) {
				boolean setLow=false;
				boolean setHigh=false;
				if (za<node.z1) setLow=true;
				if (zb>node.z2) setHigh=true;
				if (setLow) {
					deleteRange(za,node.z1-1);
					node.z1=za; 
				}
				if (setHigh) {
					deleteRange(node.z2+1,zb);		
					node.z2=zb; 
				}
				if (setLow) tryMerge(za);
				if (setHigh) tryMerge(zb+1);
				return;
			}
			
			// otherwise cut out
			deleteRange(za,zb);
			
			break;
		}
		
		// fall back to generic option - guaranteed that range is empty
		addRange(za,zb,value);
		tryMerge(za);
		tryMerge(zb+1);
	}
	
	/**
	 * Add s range with a value, guaranteed to add one node
	 * Does not check for merging!
	 */
	private void addRange(long za, long zb, T value) {
		ZNode nze=new ZNode();
		nze.object=value;
		nze.z1=za;	
		nze.z2=zb;
		head=addNode(nze,head);
	}
	
	public int countNodes() {
		return countNodes(head);
	}
	
	private int countNodes(ZNode a) {
		if (a==null) return 0;
		return 1+countNodes(a.left)+countNodes(a.right);
	}
	
	public int countArea() {
		return countArea(head);
	}
	
	private int countArea(ZNode a) {
		if (a==null) return 0;
		return 1+(int)(a.z2-a.z1)+countArea(a.left)+countArea(a.right);
	}
	
	private boolean tryMerge(long zz) {
		// if (1==1) return false;
		ZNode a=getNode(zz-1);
		if (a==null) return false; // gap
		if (a.z2>=zz) return false; // already merged
		
		ZNode b=getNode(zz);
		if (b==null) return false; //gap
		if (a.object.equals(b.object)) {
			long temp=b.z2;
			//check();
			deleteNode(b);
			//check();
			a.z2=temp; // ok since we have just deleted entire b range
			//check();
			return true;
		}
		return false;
	}
	
	private ZNode addNode(ZNode node, ZNode head) {
		if (head==null) return node;
		
		boolean left=node.compareTo(head)<0;
		if (left) {
			head.left=addNode(node,head.left);
			if (head.left.priority>head.priority) {
				return pivot(head,left);
			}
		} else {
			head.right=addNode(node,head.right);
			if (head.right.priority>head.priority) {
				return pivot(head,left);
			}
		}
		
		return head;
	}
	
	private ZNode pivot(ZNode head, boolean toLeft) {
		ZNode newHead=toLeft?head.left:head.right;
		if (toLeft) {
			head.left=newHead.right;
			newHead.right=head;
		} else {
			head.right=newHead.left;
			newHead.left=head;
		}
		return newHead;
	}
	
	/**
	 * Deletes a range, setting to null
	 * Guarantees all nodes outside this area continue to exist
	 * @param za
	 * @param zb
	 */
	public void deleteRange(long za, long zb) {
		deleteRange(za,zb,head);
	}
	
	@SuppressWarnings("unchecked")
	private void deleteRange(long za, long zb, ZNode node) {
		if (node==null) return;
		
		if ((za>node.z1)&&(zb<node.z2)) {
			// cut out hole!
			long nza=zb+1;
			long nzb=node.z2;
			
			node.z2=za-1; // shrink range
			addRange(nza,nzb,(T)node.object);
			return;
		} else {
			// delete ranges on either side 
			// this does not alter structure since can't be cutting out any holes 
			if (za<node.z1) deleteRange(za,zb,node.left);
			if (zb>node.z2) deleteRange(za,zb,node.right);
			
			// exit if no overlap
			if ((zb<node.z1)||(za>node.z2)) return;
		}
		
		// at least some overlap, not a hole....
		if ((za<=node.z1)&&(zb>=node.z2)) {
			// delete whole node
			deleteNode(node);
		} else {
			// cut off edges of node from correct side
			if (zb<node.z2) {
				node.z1=zb+1;
			} else {
				node.z2=za-1;
			}
		}
	}
	
	public void deleteNode(ZNode node) {
		head=deleteNode(node,head);
	}
	
	public ZNode deleteNode(ZNode node, ZNode head) {
		if (head==null) throw new Error("deleteNode: Node not found");
		if (node==head) {
			ZNode result= raiseUp(node);
			return result;
		} else {
			int dir=node.compareTo(head);
			if (dir<0) {
				head.left=deleteNode(node,head.left);
			} else {
				// if (dir==0) throw new Error("Duplicate z1 in deleteNode");
				head.right=deleteNode(node,head.right);
			}
		}
		return head;
	}
	
	private ZNode raiseUp(ZNode node) {
		return raiseUp(node.left,node.right);
	}
		
	// makes one of two nodes the parent of the other based on priority
	private ZNode raiseUp(ZNode a, ZNode b) {
		if (a==null) return b;
		if (b==null) return a;
		boolean raiseA=(a.priority>b.priority);
		if (raiseA) {
			a.right=raiseUp(a.right,b);
		} else {
			b.left=raiseUp(a,b.left);
		}
		return raiseA?a:b;
	}

	
	public static long calculateZ(int x, int y, int z) {
		return split3(x)+(split3(y)<<1)+(split3(z)<<2);
	}
	
	public static int extractX(long z) {
		return extractComponent(0,z);
	}
	
	public static int extractY(long z) {
		return extractComponent(1,z);
	}
	
	public static int extractZ(long z) {
		return extractComponent(2,z);
	}
	
	public static int extractComponent(int index, long z) {
		z>>=index;
		long result=0;
		
		long m;
		for (m=1; m<(BITS_POWER2); m<<=1) {
			result+=z&m;
			z=z>>2;
		}
		result+=z&m; // last bit
		if (result>=(BITS_POWER2>>1)) result-=BITS_POWER2;
		
		return (int)result;
	}
	
	public static long split3(long a) {
		long result=0;
		long m=1;
		for (int i=0; i<(BITS-1); i++) {
			result+=a&m;
			m=m<<3;
			a=a<<2;
		}
		result+=a&m; // last bit
		return result;
	}
}
