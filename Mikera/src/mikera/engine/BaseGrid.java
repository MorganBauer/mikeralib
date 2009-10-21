package mikera.engine;

public abstract class BaseGrid<T> implements Grid<T> {

	public void setBlock(int x1, int y1, int z1, int x2, int y2, int z2, T value) {
		for (int z=z1; z<=z2; z++) {
			for (int y=y1; y<=y2; y++) {
				for (int x=x1; x<=x2; x++) {
					set(x,y,z,value);
				}	
			}		
		}
	}
}
