package mikera.engine;

import mikera.util.Maths;

public class FluidSystem {
	public boolean isWaterPassable(int x, int y, int z) {
		return ((z>=0)&&(y==0)
				&&((Maths.abs(x)==1))||((z>0)&&(x==0)));
	}
	
	public static class FluidNode {
		int flowDirs=0;
		int flowX=0;
		int flowY=0;
		int flowZ=0;
		int type=0;
		int volume=0;
		int topPressure=0;
 	}
	
	public void fluidStep() {
		
		
	}
	
	public PointVisitor<Integer> pressureCalc=new PointVisitor<Integer>() {

		@Override
		public Object visit(int x, int y, int z, Integer value) {
			
			return null;
		}
		
	};
}
