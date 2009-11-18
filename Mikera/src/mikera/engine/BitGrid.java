package mikera.engine;

import mikera.util.Bits;
import mikera.util.Maths;
/**
 * Class for storing flexible 3D bit arrays
 * Stores bits in arrays of ints (4*4*2 aligned blocks)
 * 
 * @author Mike Anderson
 *
 */
public final class BitGrid implements Cloneable {
	private static final int XLOWBITS=2;
	private static final int YLOWBITS=2;
	private static final int ZLOWBITS=1;	
	
	private static final int XLOWMASK=(1<<XLOWBITS)-1;
	private static final int YLOWMASK=(1<<YLOWBITS)-1;
	private static final int ZLOWMASK=(1<<ZLOWBITS)-1;	
	
	// aligned block coordinates
	private int gx;
	private int gy;
	private int gz;
	
	// width, height and depth in blocks (not coordinates!!)
	private int gw;
	private int gh;
	private int gd;
	
	private int[] data=null;
	
	public BitGrid() {
	}
	
	public BitGrid(int x, int y, int z) {
		init(x,y,z);
	}
	
	public int width() {
		return gw<<XLOWBITS;
	}
	
	public int height() {
		return gh<<YLOWBITS;
	}
	
	public int depth() {
		return gd<<ZLOWBITS;
	}
	
	public int countSetBits() {
		int[] dt=data;
		if (dt==null) return 0;
		int result=0;
		for (int i=0; i<dt.length; i++) {
			result+=Bits.countSetBits(dt[i]);
		}
		return result;
	}
	
	public int get(int x, int y, int z) {
		if (data==null) return 0;
		if ((x<gx)||(y<gy)||(z<gz)) return 0;
		x-=gx; if (x>=width()) return 0;
		y-=gy; if (y>=height()) return 0;
		z-=gz; if (z>=depth()) return 0;
		int i=dataIndex(x,y,z);
		int bi=bitPos(x,y,z);
		return ((data[i]>>bi)&1)>0 ? 1:0;
	}
	
	public void visitSetBits(PointVisitor<Integer> pv) {
		if (data==null) return;
		int si=0;
		int tgw=gw; int tgh=gh; int tgd=gd; // make local copy to enable loop optimisation?
		for (int z=0; z<(tgd<<ZLOWBITS); z+=1<<ZLOWBITS) {
			for (int y=0; y<(tgh<<YLOWBITS); y+=1<<YLOWBITS) {
				for (int x=0; x<(tgw<<XLOWBITS); x+=1<<XLOWBITS) {
					int bv=data[si++];
					if (bv==0) continue;
					for (int i=0; i<32; i++) {
						if ((bv&15)!=0) {
							i+=3;
							bv>>=4;
							continue;
						}
						if ((bv&1)!=0) pv.visit(
								x+(i&XLOWMASK),
								y+((i>>XLOWBITS)&YLOWMASK),
								z+((i>>(XLOWBITS+YLOWBITS))&ZLOWMASK), 
								1);
						bv>>=1;
					}
				}					
			}
		}		
	}
	
	public void visitBits(PointVisitor<Integer> pv) {
		if (data==null) return;
		int si=0;
		int tgw=gw; int tgh=gh; int tgd=gd; // make local copy to enable loop optimisation?
		for (int z=0; z<(tgd<<ZLOWBITS); z+=1<<ZLOWBITS) {
			for (int y=0; y<(tgh<<YLOWBITS); y+=1<<YLOWBITS) {
				for (int x=0; x<(tgw<<XLOWBITS); x+=1<<XLOWBITS) {
					int bv=data[si++];
					for (int i=0; i<32; i++) {
						pv.visit(
								x+(i&XLOWMASK),
								y+((i>>XLOWBITS)&YLOWMASK),
								z+((i>>(XLOWBITS+YLOWBITS))&ZLOWMASK), 
								((bv&1)==1)?1:0);
						bv>>=1;
					}
				}					
			}
		}	
	}
	
	public BitGrid clone() {
		BitGrid nbg;
		try {
			nbg = (BitGrid)super.clone();
			int[] dt=data;
			if (dt!=null) {
				int[] ndt=new int[dt.length];
				System.arraycopy(dt, 0, ndt, 0,ndt.length);
				nbg.data=ndt;
			}
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
		return nbg;
	}
	
	public void clear() {
		data=null;
	}
	
	public int dataLength() {
		if (data==null) return 0;
		return data.length;
	}
	
	private void init(int x, int y, int z) {
		gx=x&(~XLOWMASK);
		gy=y&(~YLOWMASK);
		gz=z&(~ZLOWMASK);
		data=new int[1];
		gw=1; gh=1; gd=1;
	}
	
	public void growToInclude(int x, int y, int z) {
		if (data==null) {init(x,y,z); return;}
		
		// check if array needs resizing
		if ((x<gx)||(y<gy)||(z<gz)) { growToIncludeLocal(x,y,z); return; }
		
		// ok providing we don't need
		if (x>=((gx+width())|XLOWMASK)) {growToIncludeLocal(x,y,z); return; }
		if (y>=((gy+height())|YLOWMASK)) {growToIncludeLocal(x,y,z); return; }
		if (z>=((gz+depth())|ZLOWMASK)) {growToIncludeLocal(x,y,z); return; }	
	}
	
	private void growToIncludeLocal(int x, int y, int z) {	
		// assumes a change in size
		int ngx=Maths.min(gx,x)&(~XLOWMASK);
		int ngy=Maths.min(gy,y)&(~YLOWMASK);
		int ngz=Maths.min(gz,z)&(~ZLOWMASK);
		int ngw=(Maths.max(gx+width(), x+1)-ngx+XLOWMASK)>>XLOWBITS;
		int ngh=(Maths.max(gy+height(),y+1)-ngy+YLOWMASK)>>YLOWBITS;
		int ngd=(Maths.max(gz+depth(), z+1)-ngz+ZLOWMASK)>>ZLOWBITS;
		resize(ngx,ngy,ngz,ngw,ngh,ngd);
	} 
		
	private void resize(int ngx, int ngy, int ngz, int ngw, int ngh, int ngd) {
		int nl=ngw*ngh*ngd;
		int[] ndata=new int[nl];
		int si=0;
		for (int z=0; z<gd; z++) {
			for (int y=0; y<gh; y++) {
				int di=0;
				di+=((gz-ngz)>>ZLOWBITS)*ngw*ngh;
				di+=((gy-ngy)>>YLOWBITS)*ngw;
				di+=((gx-ngx)>>XLOWBITS);
				for (int x=0; x<gw; x++) {
					ndata[di++]=data[si++];
				}					
			}
		}
		data=ndata;
		gx=ngx;
		gy=ngy;
		gz=ngz;
		gw=ngw;
		gh=ngh;
		gd=ngd;
	}
	
	public void set(int x, int y, int z, int v) {
		if (data==null) {
			init(x,y,z);
			x-=gx; y-=gy; z-=gz;
		} else {
			if ((x<gx)||(y<gy)||(z<gz)) growToIncludeLocal(x,y,z);
			x-=gx; if (x>=width()) growToIncludeLocal(x+gx,y,z);
			y-=gy; if (y>=height()) growToIncludeLocal(x+gx,y+gy,z);
			z-=gz; if (z>=depth()) growToIncludeLocal(x+gx,y+gy,z+gz);		
		}
		setLocal(x,y,z,v);
	}
	
	private void setLocal(int x, int y, int z, int v) {
		int i=dataIndex(x,y,z);
		int bi=bitPos(x,y,z);
		long bv=1L<<bi;
		if (v!=0) {
			data[i]|=bv;
		} else {
			data[i]&=~bv;
		}
	}
	
	// get index relative to grid origin
	private int dataIndex(int rx, int ry, int rz) {
		return (rx>>XLOWBITS)+gw*((ry>>YLOWBITS)+gh*(rz>>ZLOWBITS));
	}
	
	public int bitPos(int x, int y, int z) {
		// fine to use either relative or absolute x,y,z
		// since makes no difference to low bits
		return (x&XLOWMASK)+((y&YLOWMASK)<<YLOWBITS)+((z&ZLOWMASK)<<(XLOWBITS+YLOWBITS));
	}
}
