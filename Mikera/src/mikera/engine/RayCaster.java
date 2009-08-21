package mikera.engine;

import mikera.math.*;
import mikera.util.Maths;

import java.util.*;

public class RayCaster {
	public static class CastNode implements Cloneable {
		private Point3i pos=new Point3i();
		private HashMap<Point3i,CastNode> children=new HashMap<Point3i,CastNode>();
	
		@Override
		public boolean equals(Object o) {
			if (o==this) return true;
			if (o instanceof CastNode) {
				CastNode cn=(CastNode)o;
				if (!cn.pos.equals(pos)) return false;
				if (!cn.children.equals(children)) return false;
				return true;
			}
			return false;
		}
		
		private boolean hasHashCode=false;
		private int hashCode=-1;
		
		@Override
		public int hashCode() {
			if (hasHashCode) return hashCode;
			int result=1+pos.hashCode();
			for (Point3i p:children.keySet()) {
				result+=children.get(p).hashCode();
			}
			hashCode=result;
			hasHashCode=true;
			return result;			
		}
		
		public int countCache() {
			return cache.size();
		}
		
		public int countDistinctPaths() {
			if (children.size()==0) return 1;
			int result=0;
			for (Point3i p:children.keySet()) {
				result+=children.get(p).countDistinctPaths();
			}
			return result;
		}
		
		public CastNode firstChild() {
			return children.entrySet().iterator().next().getValue();
			
		}
		
		public CastNode mergePath(CastNode cn) {
			if (cn.children.size()==0) return this;
			if (cn.children.size()>1) throw new Error("Not a single path!");
			CastNode child=cn.firstChild();
			CastNode current=children.get(child.pos);
			if (current!=null) {
				if(current.equals(child)) {
					return this;
				}
				child=current.mergePath(child);
			} 
			
			// copy on write
			CastNode nn=this.clone();
			nn.children.put(child.pos,intern(child));
			return intern(nn);
		}
		
		@SuppressWarnings("unchecked")
		public CastNode clone() {
			try {
				CastNode cn= (CastNode)super.clone();
				cn.children=(HashMap<Point3i, CastNode>) cn.children.clone();
				return cn;
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}
		
		protected boolean isInternedVersion() {
			CastNode icn=cache.get(this);
			return icn==this;
		}
		
		private static final HashMap<CastNode,CastNode> cache=new HashMap<CastNode,CastNode>();
		public static CastNode intern(CastNode cn) {
			if (cn==null) throw new Error("Can't intern null!");
					
			CastNode n=cache.get(cn);
			if (n==null) {
				for (Point3i p:cn.children.keySet()) {
					cn.children.put(p,intern(cn.children.get(p)));
				}

				cache.put(cn,cn);
				return cn;
			} else {
				cn=n;
				return n;
			}
		}
	}
	
	public CastNode generatePath(float dx, float dy, float dz, int len) {
		CastNode head=new CastNode();
		head.pos=new Point3i(0,0,0);
		CastNode cur=head;
		for (float d=0; d<len; d+=0.01f) {
			int x=Maths.floor(dx*d+0.5f);
			int y=Maths.floor(dy*d+0.5f);
			int z=Maths.floor(dz*d+0.5f);
			if (((cur.pos.x)==x)&&((cur.pos.y)==y)&&((cur.pos.z)==z)) continue;
			CastNode next=new CastNode();
			Point3i p=new Point3i(x,y,z);
			next.pos=p;
			cur.children.put(p, next);
			cur=next;
		}
		return head;
	}
	
	public CastNode generatePaths(int d) {
		CastNode base=new CastNode();
		base.pos.set(0,0,0);
		
		float shift=0.01f;
		for (float y=shift/2; y<1; y+=shift) {
			for (float z=shift/2; z<1; z+=shift) {
				CastNode path=generatePath(1,y,z,d);
				base=base.mergePath(path);
			}			
		}
		return base;
	}
}
