package mikera.engine;

import mikera.util.Bits;
import mikera.util.Maths;
/**
 * Class for storing flexible 3D int arrays
 * Stores arrays of ints
 * 
 * @author Mike Anderson
 *
 */
public class IntGrid implements Cloneable {
	// base coordinates
	private int gx;
	private int gy;
	private int gz;
	
	// width, height and depth in blocks (not coordinates!!)
	private int gw;
	private int gh;
	private int gd;
	
	private int[] data=null;
	
	public IntGrid() {
	}
	
	public IntGrid(int x, int y, int z) {
		init(x,y,z);
	}
	
	public int width() {
		return gw;
	}
	
	public int height() {
		return gh;
	}
	
	public int depth() {
		return gd;
	}
	
	public int countNonZero() {
		int[] dt=data;
		int result=0;
		for (int i=0; i<dt.length; i++) {
			if (dt[i]!=0) result++;
		}
		return result;
	}
	
	public int get(int x, int y, int z) {
		int i=dataIndex(x-gx,y-gy,z-gz);
		return (data[i]);
	}
	
	public void visitNonZero(PointVisitor<Integer> pv) {
		if (data==null) return;
		int si=0;
		int tgw=gw; int tgh=gh; int tgd=gd; // make local copy to enable loop optimisation?
		for (int z=0; z<tgd; z++) {
			for (int y=0; y<tgh; y++) {
				for (int x=0; x<tgw; x++) {
					int bv=data[si++];
					if (bv==0) continue;
					pv.visit(x,y,z,bv);
				}					
			}
		}		
	}
	
	public void visitBits(PointVisitor<Integer> pv) {
		if (data==null) return;
		int si=0;
		int tgw=gw; int tgh=gh; int tgd=gd; // make local copy to enable loop optimisation?
		for (int z=0; z<tgd; z++) {
			for (int y=0; y<tgh; y++) {
				for (int x=0; x<tgw; x++) {
					int bv=data[si++];
					pv.visit(x,y,z,bv);
				}					
			}
		}	
	}
	
	public IntGrid clone() {
		IntGrid nbg;
		try {
			nbg = (IntGrid)super.clone();
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
		gx=x;
		gy=y;
		gz=z;
		data=new int[1];
		gw=1; gh=1; gd=1;
	}
	
	public void growToInclude(int x, int y, int z) {
		if (data==null) {init(x,y,z); return;}
		
		// check if array needs resizing
		if ((x<gx)||(y<gy)||(z<gz)) { growToIncludeLocal(x,y,z); return; }
		
		// ok providing we don't need
		if (x>=(gx+width())) {growToIncludeLocal(x,y,z); return; }
		if (y>=(gy+height())) {growToIncludeLocal(x,y,z); return; }
		if (z>=(gz+depth())) {growToIncludeLocal(x,y,z); return; }	
	}
	
	private void growToIncludeLocal(int x, int y, int z) {	
		// assumes a change in size
		int ngx=Maths.min(gx,x);
		int ngy=Maths.min(gy,y);
		int ngz=Maths.min(gz,z);
		int ngw=(Maths.max(gx+width(), x+1)-ngx);
		int ngh=(Maths.max(gy+height(),y+1)-ngy);
		int ngd=(Maths.max(gz+depth(), z+1)-ngz);
		resize(ngx,ngy,ngz,ngw,ngh,ngd);
	} 
		
	private void resize(int ngx, int ngy, int ngz, int ngw, int ngh, int ngd) {
		int nl=ngw*ngh*ngd;
		int[] ndata=new int[nl];
		int si=0;
		int di=(gz-ngz)*ngw*ngh+(gy-ngy)*ngw+(gx-ngx);
		for (int z=0; z<gd; z++) {
			for (int y=0; y<gh; y++) {
				System.arraycopy(data, si, ndata, di, gw);
				si+=gw;
				di+=ngw;
			}
			di+=ngw*(ngh-gh);
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
			z-=gz; if (x>=depth()) growToIncludeLocal(x+gx,y+gy,z+gz);		
		}
		setLocal(x,y,z,v);
	}
	
	private void setLocal(int x, int y, int z, int v) {
		int i=dataIndex(x,y,z);
		if (v!=0) {
			data[i]=v;
		} else {
			data[i]=v;
		}
	}
	
	// get index relative to grid origin
	private int dataIndex(int rx, int ry, int rz) {
		return rx+gw*(ry+gh*rz);
	}
}
