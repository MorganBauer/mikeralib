package mikera.engine;

import java.util.*;

import mikera.util.Maths;
import mikera.util.RankedQueue;

public class PathFinder {
	
	
	public static class PathNode  {
		public int x;
		public int y;
		public int z;
		public float remainingDist=0;
		public float travelledDist=0;
		public PathNode last=null;
		public byte triedDirs=-1;
		
		public float estimatedDist() {
			return remainingDist+travelledDist;
		}
	}
	
	public abstract static class PathFunction {
		public abstract float moveCost(int x, int y, int z, byte dir);
		
		
	}
	
	public static float estimate(int x, int y, int z, int tx, int ty, int tz) {
		int dx=Maths.abs(tx-x);
		int dy=Maths.abs(ty-y);
		int dz=Maths.abs(tz-z);
		
		return Maths.max(dx,dy,dz);
	}
	
	//protected PriorityQueue<PathNode> nodes=new PriorityQueue<PathNode>();
	protected RankedQueue<PathNode> nodes=new RankedQueue<PathNode>();
	
	public void clear() {
		
	}
	
	public void pathFind(int x,int y, int z, int tx, int ty, int tz, PathFunction pf) {
		nodes.clear();
		
		PathNode pn=new PathNode();
		pn.x=x;
		pn.y=y;
		pn.z=z;
		pn.remainingDist=estimate(x,y,z,tx,ty,tz);
		
	}
}
