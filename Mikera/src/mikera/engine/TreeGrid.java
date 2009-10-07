package mikera.engine;

public class TreeGrid<T> extends BaseGrid<T> {
	private static final int TOPMASK=3<<18;
	
	final Object[] data=new Object[64];
	
	@SuppressWarnings("unchecked")
	public T get(int x, int y, int z) {
		int shift=18;
		TreeGrid<T> head=this;
		while (shift>0) {
			int lx=(x>>shift)&3;
			int ly=(y>>shift)&3;
			int lz=(z>>shift)&3;
			int li=lx+(ly<<2)+(lz<<4);
			Object d=head.data[li];
			if (d==null) return null;
			if (!(d instanceof TreeGrid<?>)) {
				return (T)d;
			}
			shift-=2;
			head=(TreeGrid<T>)d;
		}
		throw new Error("This shouldn't happen!!");
	}
	
	public TreeGrid() {
		
	}
	
	public TreeGrid(T defaultvalue) {
		for (int i=0; i<data.length; i++) {
			data[i]=defaultvalue;
		}
	}

	@SuppressWarnings("unchecked")
	public void set(int x, int y, int z, T value) {
		int shift=18;
		TreeGrid<T> head=this;
		while (shift>0) {
			int lx=(x>>shift)&3;
			int ly=(y>>shift)&3;
			int lz=(z>>shift)&3;
			int li=lx+(ly<<2)+(lz<<4);
			Object d=head.data[li];
			if (d==null) {
				if (value==null) return;
				d=new TreeGrid<T>();
				head.data[li]=d;
			}
			if (shift==0) {
				head.data[li]=value;
				if (head.isSolid(value)) solidify(x,y,z,18);
				return;
			} else if (!(d instanceof TreeGrid<?>)) {
				if (d.equals(value)) return;
				d=new TreeGrid<T>((T)d);
				head.data[li]=d;				
			}
			shift-=2;
			head=(TreeGrid<T>)d;
		}
		throw new Error("This shouldn't happen!!");
	}
	
	private boolean isSolid(T value) {
		for (int i=0; i<data.length; i++) {
			Object d=data[i];
			if (!((d==value)||((d!=null)&&(d.equals(value))))) {
				return false;
			}
		}
		return true;
	}
	
	private void solidify(int x, int y, int z, int shift) {
		int lx=(x>>shift)&3;
		int ly=(y>>shift)&3;
		int lz=(z>>shift)&3;
		int li=lx+(ly<<2)+(lz<<4);
		Object d=data[li];
		// TODO
		if (shift>0) solidify(x,y,z,shift-1);
		
	}

}
