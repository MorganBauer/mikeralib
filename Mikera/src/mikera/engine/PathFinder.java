package mikera.engine;

import java.util.*;

import mikera.util.Maths;

public class PathFinder {
	private static class PathNode implements Comparable<PathNode> {
		public float h=0;
		public float g=0;
		public byte lastDir=0;
		public byte triedDirs=-1;
		
		public float total() {
			return h+g;
		}
		
		public int compareTo(PathNode arg0) {
			return Maths.sign(total()-arg0.total());
		}
	}
	
	public static interface PathFunction {
		public float moveCost(int x, int y, int z, byte dir);
		
		
	}
	
	public static float estimate(int x, int y, int z, int tx, int ty, int tz) {
		int dx=Maths.abs(tx-x);
		int dy=Maths.abs(ty-y);
		int dz=Maths.abs(tz-z);
		
		return Maths.max(dx,dy,dz);
	}
	
	private PriorityQueue<PathNode> nodes=new PriorityQueue<PathNode>();
	
	public void clear() {
		
	}
	
	public void pathFind(int x,int y, int z, int tx, int ty, int tz, PathFunction pf) {
		PathNode pn=new PathNode();
		
		
	}
}
