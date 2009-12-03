package mikera.engine;

import mikera.util.Maths;

public class Dir {
	public static final byte C=0;
	public static final byte N=1;
	public static final byte S=2;
	public static final byte E=3;
	public static final byte W=6;
	public static final byte NE=N+E;
	public static final byte NW=N+W;
	public static final byte SE=S+E;
	public static final byte SW=S+W;	
	
	public static final byte U=9;
	public static final byte UN=U+N;
	public static final byte US=U+S;
	public static final byte UE=U+E;
	public static final byte UW=U+W;
	public static final byte UNE=UN+E;
	public static final byte UNW=UN+W;
	public static final byte USE=US+E;
	public static final byte USW=US+W;	
	
	public static final byte D=18;
	public static final byte DN=D+N;
	public static final byte DS=D+S;
	public static final byte DE=D+E;
	public static final byte DW=D+W;
	public static final byte DNE=DN+E;
	public static final byte DNW=DN+W;
	public static final byte DSE=DS+E;
	public static final byte DSW=DS+W;	
	
	public static final byte MAX_DIR=DSW+1;

	public static final byte[] DX={0, 0, 0, 1, 1, 1,-1,-1,-1, 0, 0, 0, 1, 1, 1,-1,-1,-1, 0, 0, 0, 1, 1, 1,-1,-1,-1};
	public static final byte[] DY={0, 1,-1, 0, 1,-1, 0, 1,-1, 0, 1,-1, 0, 1,-1, 0, 1,-1, 0, 1,-1, 0, 1,-1, 0, 1,-1};
	public static final byte[] DZ={0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static final float[] DIST=new float[MAX_DIR];
	public static final int[] MASK=new int[MAX_DIR];
	
	public static final byte[] OTHOGONAL_DIRECTIONS={N,S,E,W,U,D};
	public static final byte[] DISTORDER_DIRECTIONS={C,N,S,E,W,U,D,NE,NW,SE,SW,UN,US,DN,DS,UE,UW,DE,DW,UNE,UNW,DNE,DNW,USE,USW,DSE,DSW};
	public static final byte[] ALL_DIRECTIONS_3D   ={C,N,S,E,NE,SE,W,NW,SW,U,UN,US,UE,UNE,USE,UW,UNW,USW,D,DN,DS,DE,DNE,DSE,DW,DNW,DSW};
	public static final byte[] ALL_DIRECTIONS_2D   ={C,N,S,E,NE,SE,W,NW,SW};
	public static final byte[] REVERSE_DIRECTIONS  =new byte[27];
	public static final Integer[] ALL_DIRECTIONS_INTEGER ={C,N,S,E,NE,SE,W,NW,SW,U,UN,US,UE,UNE,USE,UW,UNW,USW,D,DN,DS,DE,DNE,DSE,DW,DNW,DSW};
	
	public static long addToZ(long z, int dir) {
		return Octreap.calculateZ(Octreap.extractX(z)+DX[dir], Octreap.extractY(z)+DY[dir], Octreap.extractZ(z)+DZ[dir]);
	}
	
	public static int dirMask(int dir) {
		return 1<<dir;
	}
	
	public static void visitDirections(int dirSet, PointVisitor<Integer> p, int x, int y, int z) {
		int mask=1;
		for (int i=0; i<MAX_DIR; i++) {
			if ((dirSet&mask)!=0) {
				p.visit(x+DX[i], y+DY[i], z+DZ[i], ALL_DIRECTIONS_INTEGER[i]);
			}
			mask<<=1;
		}	
	}
	
	public static final byte getDir(int dx, int dy, int dz) {
		byte d=0;
		
		if (dx<0) d+=W;
		else if (dx>0) d+=E;
		
		if (dy<0) d+=S;
		else if (dy>0) d+=N;
		
		if (dz<0) d+=D;
		else if (dz>0) d+=U;
		
		return d;
	}
	 
	static {
		for (int i=0; i<MAX_DIR; i++) {
			DIST[i]=Maths.sqrt(Maths.square(DX[i])+Maths.square(DY[i])+Maths.square(DZ[i]));
		}
		
		for (int i=0; i<MAX_DIR; i++) {
			MASK[i]=dirMask(i);
		}

		for (int i=0; i<MAX_DIR; i++) {
			REVERSE_DIRECTIONS[i]=getDir(-DX[i],-DY[i],-DZ[i]);
		}
	}
}
