package mikera.engine;

import mikera.util.Maths;

public class WaterSystem {
	public boolean isWaterPassable(int x, int y, int z) {
		return ((z>=0)&&(y==0)
				&&((Maths.abs(x)==1))||((z>0)&&(x==0)));
	}
	
	public TreeGrid<Integer> water=new TreeGrid<Integer>();
	public TreeGrid<Integer> topPressure=new TreeGrid<Integer>();
	public TreeGrid<Integer> flowX=new TreeGrid<Integer>();
	public TreeGrid<Integer> flowY=new TreeGrid<Integer>();
	public TreeGrid<Integer> flowZ=new TreeGrid<Integer>();
	
	
	public void waterStep() {
		
		
	}
	
	public PointVisitor<Integer> pressureCalc=new PointVisitor<Integer>() {

		@Override
		public Object visit(int x, int y, int z, Integer value) {
			
			return null;
		}
		
	};
}
