package mikera.engine;

public class ChunkGrid<T> extends BaseGrid<T> {
	private static final int LEVEL_BITS=8;
	private static final int LEVEL_SIZE=1<<LEVEL_BITS;
	private static final int MAX_BITS=60;
	
	private static class ChunkNode<T> {
		private final int shift;
		private final long start;
		private final Object[] data;
		
		protected ChunkNode(long startZ, int shiftLevel) {
			shift=shiftLevel;
			start=startZ;
			int size=LEVEL_SIZE;
			data=new Object[LEVEL_SIZE];
		}

		public void clear() {
			for (int i=0; i<data.length; i++) {
				data[i]=null;
			}
		}

		public T get(long zz) {
			int off=(int)((zz-start)>>shift);
			Object d=data[off];
			if (shift==0) {
				return (T)d;
			} else {
				if (d instanceof ChunkNode) {
					
				} else {
					
				}
			}
		}

		public boolean inRange(long zz) {
			// TODO Auto-generated method stub
			return false;
		}

		public ChunkNode<T> levelUp() {
			// TODO Auto-generated method stub
			return null;
		}

		public void visitBlocks(BlockVisitor<T> bf) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	private ChunkNode node=new ChunkNode(0);
	
	@Override
	public void clear() {
		node=new ChunkNode(0);

	}

	@Override
	public void clearContents() {
		node.clear();
	}

	@Override
	public int countNonNull() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T get(int x, int y, int z) {
		long zz=Octreap.calculateZ(x, y, z);
		return node.get(zz);
	}

	@Override
	public void set(int x, int y, int z, T value) {
		long zz=calculateZ(x, y, z);
		while (!node.inRange(zz)) {
			node=node.levelUp();
		}
	}

	private long calculateZ(int x, int y, int z) {
		return Octreap.calculateZ(x, y, z);
	}

	@Override
	public void visitBlocks(BlockVisitor<T> bf) {
		node.visitBlocks(bf);
	}

}
