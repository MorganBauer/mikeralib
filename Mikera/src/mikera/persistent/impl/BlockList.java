package mikera.persistent.impl;

import java.util.List;

import mikera.persistent.ListFactory;
import mikera.persistent.PersistentList;
import mikera.util.TODOException;
import mikera.util.emptyobjects.NullArrays;
import mikera.util.emptyobjects.NullList;

public final class BlockList<T> extends BasePersistentList<T> {
	private static final long serialVersionUID = 7210896608719053578L;

	protected static final int DEFAULT_SHIFT=ListFactory.TUPLE_BUILD_BITS;
	protected static final int SHIFT_STEP=4;
	protected static final int SHIFT_MASK=(1<<SHIFT_STEP)-1;
	
	private final int shift;
	private final int size;
	private final int offset;
	private final PersistentList<T>[] blocks;
	
	@SuppressWarnings("unchecked")
	public static final BlockList EMPTY_BLOCKLIST=new BlockList(ListFactory.NULL_PERSISTENT_LIST_ARRAY,DEFAULT_SHIFT,0,0);
	
	public static <T> BlockList<T> create(List<T> list) {
		return create(list,0,list.size());
	}
	
	public static <T> BlockList<T> create(List<T> list, int fromIndex, int toIndex) {
		int size=toIndex-fromIndex;
		if (size<0) throw new IllegalArgumentException();
		
		int shift=DEFAULT_SHIFT;
		while ((1<<(shift+SHIFT_STEP))<size) {
			shift+=SHIFT_STEP;
		}
		return create(list,fromIndex,toIndex,shift);
	}
		
	@SuppressWarnings("unchecked")
	private static <T> BlockList<T> create(List<T> list, int fromIndex, int toIndex, int shift) {
		if (shift>DEFAULT_SHIFT) {
			int size=toIndex-fromIndex;
			int numBlocks=numBlocks(size,shift);
		
			PersistentList<T>[] bs=(PersistentList<T>[]) new PersistentList<?>[numBlocks];
			for (int i=0; i<(numBlocks-1); i++) {
				bs[i]=create(
						list,
						fromIndex+(i<<shift), 
						fromIndex+((i+1)<<shift),
						shift-SHIFT_STEP);
			}
			bs[numBlocks-1]=create(
					list,
					fromIndex+((numBlocks-1)<<shift), 
					fromIndex+size,
					shift-SHIFT_STEP);
		
			return new BlockList<T>(bs,shift,size,0);			
		} else {
			return createLowestLevel(list,fromIndex, toIndex,DEFAULT_SHIFT);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> BlockList<T> createLowestLevel(List<T> list, int fromIndex, int toIndex,int shift) {
		int size=toIndex-fromIndex;
		int numBlocks=numBlocks(size,shift);
	
		PersistentList<T>[] bs=(PersistentList<T>[]) new PersistentList<?>[numBlocks];
		for (int i=0; i<(numBlocks-1); i++) {
			bs[i]=ListFactory.subList(
					list,
					fromIndex+(i<<shift), 
					fromIndex+((i+1)<<shift));
		}
		bs[numBlocks-1]=ListFactory.subList(
				list,
				fromIndex+((numBlocks-1)<<shift), 
				fromIndex+size);
	
		return new BlockList(bs,shift,size,0);
	}
	
	private static final int numBlocks(int size, int shift) {
		return 1+((size-1)>>shift);
	}
	
	private BlockList(PersistentList<T>[] bs, int sh, int sz, int off) {
		blocks=bs;
		shift=sh;
		size=sz;
		offset=off;
	}
	
	@Override
	public T get(int i) {
		if ((i<0)||(i>=size)) throw new IndexOutOfBoundsException();
		int pos=i+offset;
		int bi=(pos>>shift);
		int bpos=pos&((1<<shift)-1);
		return blocks[bi].get(bpos);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	private int blockStart(int blockIndex) {
		return blockIndex<<shift;
	}
	
	@Override
	public PersistentList<T> subList(int fromIndex, int toIndex) {
		if ((fromIndex<0)||(toIndex>size)) throw new IndexOutOfBoundsException();
		if ((fromIndex>=toIndex)) {
			if (toIndex==fromIndex) return ListFactory.emptyList();
			throw new IllegalArgumentException();
		}
		if ((fromIndex==0)&&(toIndex==size)) return this;
		
		// see if we can take a subset of a single block
		int fromBlock=(fromIndex+offset)>>shift;
		int toBlock=(toIndex-1+offset)>>shift;
		if ((fromBlock)==(toBlock)) {
			int blockStart=blockStart(fromBlock);
			return blocks[fromBlock].subList(fromIndex+offset-blockStart, toIndex+offset-blockStart);
		}
		
		return subBlockList(fromIndex,toIndex);
	}
	
	/**
	 * Gets a subList as a BlockList with the same shift
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	private BlockList<T> subBlockList(int fromIndex, int toIndex) {

		return new BlockList<T>(blocks,shift,(toIndex-fromIndex),fromIndex+offset);	
	}
}
