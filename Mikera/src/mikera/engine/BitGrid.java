package mikera.engine;

import mikera.util.Maths;
/**
 * Class for storing flexible 3D bit arrays
 * Stores bits in arrays of ints (4*4*2 aligned blocks
 * 
 * @author Mike Anderson
 *
 */
public class BitGrid {
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
		return gw;
	}
	
	public int height() {
		return gh;
	}
	
	public int depth() {
		return gd;
	}
	
	public int get(int x, int y, int z) {
		if (data==null) return 0;
		if ((x<gx)||(y<gy)||(z<gz)) return 0;
		x-=gx; if (x>=gw) return 0;
		y-=gy; if (y>=gh) return 0;
		z-=gz; if (x>=gd) return 0;
		int i=index(x,y,z);
		int bi=bitPos(x,y,z);
		return ((data[i]>>bi)&1)>0 ? 1:0;
	}
	
	private void init(int x, int y, int z) {
		gx=x&(~XLOWMASK);
		gy=y&(~YLOWMASK);
		gz=z&(~ZLOWMASK);
		data=new int[1];
		gw=4; gh=4; gd=2;
	}
	
	public void growToInclude(int x, int y, int z) {
		// check if array needs resizing
		if ((x<gx)||(y<gy)||(z<gz)) { growToIncludeLocal(x,y,z); return; }
		
		// ok providing we don't need
		if (x>=((gx+gw)|XLOWMASK)) {growToIncludeLocal(x,y,z); return; }
		if (y>=((gy+gh)|YLOWMASK)) {growToIncludeLocal(x,y,z); return; }
		if (z>=((gz+gd)|ZLOWMASK)) {growToIncludeLocal(x,y,z); return; }	
		// extend gw,gh,gd if necessary
		if (x>=(gx+gw)) gw=x-gx+1;
		if (y>=(gy+gh)) gh=x-gy+1;
		if (z>=(gz+gd)) gd=x-gz+1;
	}
	
	private void growToIncludeLocal(int x, int y, int z) {
		// assumes a change in size
		int ngx=Maths.min(gx,x);
		int ngw=Maths.max(gx+gw, x+1)-ngx;
		int ngy=Maths.min(gy,y);
		int ngh=Maths.max(gy+gh, y+1)-ngy;
		int ngz=Maths.min(gz,z);
		int ngd=Maths.max(gz+gd, z+1)-ngz;
		int nl=dataSize(ngw,ngh,ngd);
		int[] ndata=new int[nl];
		if (ndata.length>0) throw new Error("TODO manage resizing");
		data=ndata;
		gx=ngx;
		gy=ngy;
		gz=ngz;
	}
	
	public void set(int x, int y, int z, int v) {
		if ((x<gx)||(y<gy)||(z<gz)) growToIncludeLocal(x,y,z);
		x-=gx; if (x>=gw) growToIncludeLocal(x+gx,y,z);
		y-=gy; if (y>=gh) growToIncludeLocal(x+gx,y+gy,z);
		z-=gz; if (x>=gd) growToIncludeLocal(x+gx,y+gy,z+gz);
		int i=index(x,y,z);
		int bi=bitPos(x,y,z);
		long bv=1L<<bi;
		if (v!=0) {
			data[i]|=bv;
		} else {
			data[i]&=~bv;
		}
	}
	
	public int dataSize(int w, int h, int d) {
		// note: gx,gy,gz assumed to be aligned (zero low bits)
		// so we can safely do this
		return (w+XLOWMASK>>XLOWBITS)*((h+YLOWMASK)>>YLOWBITS)*((d+ZLOWMASK)>>ZLOWBITS);
	}
	
	// get index relative to grid origin
	private int index(int rx, int ry, int rz) {
		return (rx>>XLOWBITS)+(ry>>YLOWBITS)*gw+(rz>>ZLOWBITS)*gw*gh;
	}
	
	public int bitPos(int x, int y, int z) {
		// fine to use either relative or absolute x,y,z
		// since makes no difference to low bits
		return (x&XLOWMASK)+((y&YLOWMASK)<<YLOWBITS)+((z&ZLOWMASK)<<(XLOWBITS+YLOWBITS));
	}
}
