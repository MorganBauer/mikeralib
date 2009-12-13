package mikera.engine;

import java.util.*;

import mikera.util.Maths;
import mikera.util.RankedQueue;

public final class PathFinder {
	public int MAX_STEPS=10000;
	
	protected static final boolean OPTIMISTIC_SEARCH=true;
	protected static final boolean CACHE_PATHNODES=true;
	
	// priority queue of nodes to search
	// sorted by estimated total distance (cost + heuristic)
	protected RankedQueue<PathNode> nodes=new RankedQueue<PathNode>();
	protected TreeGrid<PathNode> map=new TreeGrid<PathNode>();
	protected ArrayList<PathNode> cache=null;
	
	private HeuristicFunction heuristicFunction=null;
	private CostFunction costFunction=null;
	protected PathNode found=null;
	
	public int nodeCount=0;
	public int costCount=0;
	
	public PathFinder() {
		if (CACHE_PATHNODES) {
			cache=new ArrayList<PathNode>();
		}
	}
	

	public static class PathNode  {
		public int x;
		public int y;
		public int z;
		public float heuristic;
		public float travelled;
		public PathNode last;
		public byte baseDir;
		public byte checkedDirCount;
		
		public PathNode() {
			init();
		}
		
		public float estimatedDist() {
			return heuristic+travelled;
		}
		
		public void init() {
			heuristic=0;
			travelled=0;
			last=null;
			baseDir=0;
			checkedDirCount=0;
		}
	}
	
	/**
	 * Class to override to provide a cost function
	 */
	public abstract static class CostFunction {
		public float moveCost(int x, int y, int z, byte dir) {
			int tx=x+Dir.DX[dir];
			int ty=y+Dir.DY[dir];
			int tz=z+Dir.DZ[dir];
			return moveCost(x,y,z,tx,ty,tz);
		}

		public abstract float moveCost(int x, int y, int z, int tx, int ty, int tz) ;

	}
	
	/**
	 * Class to override to provide a cost function
	 */
	public abstract static class HeuristicFunction {
		/**
		 * Method to calculate heuristic value
		 * Should return 
		 *     <=0 if target found
		 *     Estimated distance to target otherwise
		 */
		public abstract float calcHeuristic(int x, int y, int z);
		
		public byte defaultDirection(int x, int y, int z) {
			return 0;
		}
	}
	
	public static class StandardHeuristic extends HeuristicFunction {
		int tx, ty, tz;
		public StandardHeuristic(int targetX, int targetY, int targetZ) {
			tx=targetX;
			ty=targetY;
			tz=targetZ;
		}
		
		@Override
		public float calcHeuristic(int x, int y, int z) {
			return estimate(x,y,z,tx,ty,tz);
			
		}	
		
		@Override
		public byte defaultDirection(int x, int y, int z) {
			return Dir.getDir(tx-x,ty-y,tz-z);
		}
	}
	
	public HeuristicFunction targetFunction(int tx, int ty, int tz) {
		return new StandardHeuristic(tx,ty,tz);
	}
	
	public static float estimate(int x, int y, int z, int tx, int ty, int tz) {
		int dx=Maths.abs(tx-x);
		int dy=Maths.abs(ty-y);
		int dz=Maths.abs(tz-z);
		return dx+dy+dz;
	}
	
	public boolean isFound() {
		return (found!=null);
	}
	
	public PathNode foundNode() {
		return found;
	}
	
	public void clear() {
		nodes.clear();
		if (CACHE_PATHNODES) {
			map.visitPoints(new PointVisitor<PathNode>() {
				@Override
				public Object visit(int x, int y, int z, PathNode value) {
					reclaim(value);
					return null;
				}		
			});
		}
		map.clear();
		found=null;
		costCount=0;
		nodeCount=0;
	}
	
	private void reclaim(PathNode pn) {
		pn.init();
		cache.add(pn);
	}
	
	public void pathFind(int x, int y, int z, int tx, int ty, int tz) {
		HeuristicFunction tf=targetFunction(tx,ty,tz);
		pathFind(x,y,z,tf,costFunction);
	}

	
	public void pathFind(int x, int y, int z) {
		pathFind(x,y,z,heuristicFunction,costFunction);
	}
	
	public void pathFind(int x,int y, int z, HeuristicFunction hf, CostFunction pf) {
		clear();
		setCostFunction(pf);
		setHeuristicFunction(hf);
		setupPathFind(x,y,z);
		findPath();
	}
	
	private void findPath() {
		int steps=0;
		while (!isFound()) {
			PathNode node=nodes.poll();
			if (node==null) return;
			
			for (byte i=node.checkedDirCount; i<Dir.MAX_DIR; i++) {
				if ((steps++)>=MAX_STEPS) return;
					
				// get direction to check in sequence
				// based on default direction of heuristic function
				byte checkDir=Dir.getClosestDir(node.baseDir, i);
				
				// check the given direction
				PathNode tn=tryDir(node,checkDir);
						
				if ((OPTIMISTIC_SEARCH)&&(tn!=null)) {
					// fast path search
					if (tn.estimatedDist()<=node.estimatedDist()) {
						int nextIndex=i+1;
						// requeue node
						if (nextIndex<Dir.MAX_DIR) {
							node.checkedDirCount=(byte) nextIndex;
							addNode(node);
						}
						break;
					}
				}
			}		
		}
	}
	
	private PathNode tryDir(PathNode node, byte dir) {
		int x=node.x; int tx=x+Dir.DX[dir];
		int y=node.y; int ty=y+Dir.DY[dir];
		int z=node.z; int tz=z+Dir.DZ[dir];
		
		float travelled=node.travelled;
		float cost=getCostFunction().moveCost(x, y, z, tx,ty,tz);
		costCount++;
		if (cost<0) return null;
		
		PathNode target=map.get(tx, ty, tz);
		
		if (target==null) {
			PathNode pn=createPathNode(tx,ty,tz);
			pn.last=node;
			float heuristic=getHeuristicFunction().calcHeuristic(tx, ty, tz);
			pn.heuristic=heuristic;
			if (heuristic<=0) {
				setFound(pn);
			}
			pn.travelled=travelled+cost;
			map.set(tx,ty,tz,pn);
			addNode(pn);
			
			return pn;
		} else {
			if (target.travelled<=(cost+travelled)) return target;
			
			// we have found a shorter path to target
			target.last=node;
			target.travelled=cost+travelled;
			target.checkedDirCount=0;
			
			// ensure place on search list
			addNode(target);
			return target;
		}
	}
		
	private void setFound(PathNode pn) {
		found = pn;
		
	}
	
	/**
	 * add or re-add a node to the search list
	 * @param pn
	 */
	private void addNode(PathNode pn) {
		nodes.add(pn,pn.estimatedDist());
		nodeCount++;
	}

	private void setupPathFind(int x,int y, int z) {
		PathNode pn=createPathNode(x,y,z);
		pn.heuristic=getHeuristicFunction().calcHeuristic(x, y, z);
		addNode(pn);
	}


	private PathNode createPathNode(int x, int y, int z) {
		PathNode pn;
		if (CACHE_PATHNODES) {
			int cs=cache.size();
			if (cs>0) {
				pn=cache.remove(cs-1);
			} else {
				pn=new PathNode();
			}
		} else {
			pn= new PathNode();
		}
		pn.baseDir=heuristicFunction.defaultDirection(x,y,z);
		pn.x=x;
		pn.y=y;
		pn.z=z;
		return pn;
	}

	public void setHeuristicFunction(HeuristicFunction heuristicFunction) {
		this.heuristicFunction = heuristicFunction;
	}


	public HeuristicFunction getHeuristicFunction() {
		return heuristicFunction;
	}


	public void setCostFunction(CostFunction costFunction) {
		this.costFunction = costFunction;
	}


	public CostFunction getCostFunction() {
		return costFunction;
	}


}
