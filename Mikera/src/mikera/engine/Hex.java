package mikera.engine;

import mikera.image.ImageUtils;
import mikera.math.*;
import mikera.util.Maths;

/**
 * Utility functions for hexagonal grids
 * 
 * @author Mike
 *
 */
public class Hex {
	private static final float RATIO=0.866025404f;
	
	public static final int[] HEX_DX={0,1,1,0,-1,-1};
	public static final int[] HEX_DY={-1, -1, 0 , 1, 1, 0};
	
	// Location functions are hex-grid coordinates
	// Positions are logical float-coordinates with (0,0) at centre of unit-height hex at (0,0)
	// screen values are then simple multiples of logical positions
	
	public static int dx(int dir) {
		return HEX_DX[Maths.mod(dir, 6)];
	}
	public static int dy(int dir) {
		return HEX_DY[Maths.mod(dir, 6)];
	}
	
	public static int toLocationX(float px, float py) {
		float b=Maths.floor(py+px/(RATIO*2.0f/3.0f));
		float c=Maths.floor(py-px/(RATIO*2.0f/3.0f));	
		
		return (int)Math.floor((1+b-c)/3);
	}
	
	public static int toLocationY(float px, float py) {
		float a=Maths.floor(py*2.0f);
		float b=Maths.floor(py+px/(RATIO*2.0f/3.0f));
		float c=Maths.floor(py-px/(RATIO*2.0f/3.0f));	
		
		return (int)Math.floor((4+3*a-b+c)/6);
	}
	
	public static int direction (int sx, int sy, int tx, int ty) {
		return direction (tx-sx,ty-sy);
	}

	
	public static int direction (int dx, int dy) {
		int a = (dx*2+dy);
		int b = (dx -dy);
		int c = (dx +2*dy);
		if (b>=0) {
			if (c>=0) {
				return (b==0)?3:2;
			} else {
				if (a>=0) {
					return 1;
				} else {
					return 0;
				}
			}
		} else {
			if (c<=0) {
				return 5;
			} else {
				if (a<=0) {
					return 4;
				} else {
					return 3;
				}
			}
		}
	}
	
	public static float toPositionX (int lx, int ly) {
		return lx*RATIO;
	}
	
	public static float toPositionY (int lx, int ly) {
		return ly+lx*0.5f;
	}
	
	/**
	 *  Hex distance calculation
	 */
	public static int distance(int x1, int y1, int x2, int y2) {
		int dx=x2-x1;
		int dy=y2-y1;
		
		if (dx*dy>0) {
			return Maths.abs(dx+dy);
		} else {
			return Maths.max(Maths.abs(dx),Maths.abs(dy));
		}
	}
	
	
	// Main function for testing
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		VectorFunction vf=new BaseVectorFunction(2,1) {
			@Override
			public void calculate(Vector input, Vector output) {
				float x=input.data[0]*16.0f;
				float y=input.data[1]*16.0f;
				int lx=Hex.toLocationX(x,y);
				int ly=Hex.toLocationY(x,y);
				float px=Hex.toPositionX(lx, ly);
				float py=Hex.toPositionY(lx, ly);
				int d55=Hex.distance(lx,ly,5,5);
				boolean centre=(Vector.lengthSquared(px-x,py-y)<0.02f); 
				//output.data[0]=Maths.frac(0.3f*lx + 0.085f*ly +(centre?0.2f:0));
				output.data[0]=Maths.frac(d55*0.05f);
			}			
		};
		
		ImageUtils.displayAndExit(vf);
	}
}
