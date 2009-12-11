package mikera.engine;

import mikera.math.*;
import mikera.util.Bits;
import mikera.util.Maths;

import java.util.*;

public class RayCaster {
	private CastFunction castFunction;
	
	public static abstract class CastFunction {
		public abstract boolean visit(int x, int y, int z);
	}
	
	public void setCastFunction(CastFunction castFunction) {
		this.castFunction = castFunction;
	}

	public CastFunction getCastFunction() {
		return castFunction;
	}	
	
	
	
	public void cast(int cx, int cy, int cz) {
		castLocal(cx,cy,cy,0,0,0);
	}
	
	private void castLocal(int cx, int cy, int cz, int dx, int dy, int dz) {
		boolean shouldContinue=castFunction.visit(cx+dx, cy+dy, cz+dz);
		
		if (!shouldContinue) return;
		
		int ax=Maths.abs(dx);
		int ay=Maths.abs(dy);
		int az=Maths.abs(dz);
		byte dirx=((dx>=0)?Dir.E:Dir.W);
		byte diry=((dy>=0)?Dir.N:Dir.S);
		byte dirz=((dz>=0)?Dir.U:Dir.D);
		
		int dirs=0;
		if ((dx==0)&&(dy==0)&&(dz==0)) {
			dirs=Dir.ALL_DIRS_MASK;
		} else {
			if ((ax>=ay)&&(ax>=az)) dirs|=getDir(ax,ay,az,dirx,diry,dirz);
			
			if ((ay>=ax)&&(ay>=az)) dirs|=getDir(ay,ax,az,diry,dirx,dirz);
			if ((az>=ax)&&(az>=ay)) dirs|=getDir(az,ax,ay,dirz,dirx,diry);
		}
		
		for (byte dir=1; dir<=Dir.MAX_DIR; dir++) {
			dirs=dirs>>1;
			if ((dirs&1)!=0) {
				castLocal(cx,cy,cz,dx+Dir.dx(dir),dy+Dir.dy(dir),dz+Dir.dz(dir));
			}
		}
	}

	private int getDir(int a1, int a2, int a3, byte d1, byte d2, byte d3) {
		int dirs=0;
		dirs|=getDir2(a1,a2,a3,d1,d2,d3);
		if (a2==0) {
			dirs|=getDir2(a1,a2,a3,d1,Dir.reverse(d2),d3);
		}
		if (a3==0) {
			dirs|=getDir2(a1,a2,a3,d1,d2,Dir.reverse(d3));
		}
		if ((a3==0)&&(a2==0)) {
			dirs|=getDir2(a1,a2,a3,d1,Dir.reverse(d2),Dir.reverse(d3));
		}


		return dirs;
	}
	
	private int getDir2(int a1, int a2, int a3, byte d1, byte d2, byte d3) {
		// point at which split occurs (distance from a1 axis)
		int splitPoint=Bits.roundUpToPowerOfTwo(a1+2)-2-a1;
		int dirs=0;
		
		// straight ahead
		if (a2<=splitPoint) dirs|=Dir.dirMask(d1);
		if (a3<=splitPoint) dirs|=Dir.dirMask(d1);
		
		// fan out
		if (a2>=splitPoint) dirs|=Dir.dirMask(d1+d2);
		if (a3>=splitPoint) dirs|=Dir.dirMask(d1+d3);
	
		// diagonal
		if ((a3>=splitPoint)&&(a2>=splitPoint)) dirs|=Dir.dirMask(d1+d2+d3);
		
		return dirs;
	}
}
