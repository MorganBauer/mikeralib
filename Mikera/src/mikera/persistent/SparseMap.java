package mikera.persistent;

public final class SparseMap<T> extends PersistentObject {
	final int bits;
	final Object[] data;
	
	@SuppressWarnings("unchecked")
	private static SparseMap<?> EMPTY=new SparseMap(2, new Object[16]);
	
	private SparseMap(int n, Object[] objects) {
		bits=n;
		data=objects;
	}
	
	public SparseMap() {
		bits=2;
		data=EMPTY.data;
	}

	@SuppressWarnings("unchecked")
	public SparseMap<T> create() {
		return (SparseMap<T>)EMPTY;
	}
	
	public int countNotNull() {
		int result=0;
		if (bits==2) {
			for (int i=0; i<16; i++) {
				Object o=data[i];
				if (o!=null) {
					result++;
				}
			}
		} else {
			for (int i=0; i<16; i++) {
				Object o=data[i];
				if (o!=null) {
					result+=((SparseMap<?>)o).countNotNull();
				}
			}
		}
		return result;
	}
	
	public T get(int x, int y) {	
		int bo=baseOffset(bits);
		int ix=x-bo;
		int iy=y-bo;
		if ((ix<0)||(iy<0)) return null;
		int size=1<<bits;
		if ((ix>=size)||(iy>=size)) return null;
		return getInternal(ix,iy);
	}
	
	// note uses zero based positioning
	@SuppressWarnings("unchecked")
	private T getInternal(int ix, int iy) {	
		if (bits==2) {		
			return (T)data[getIndex(ix,iy)];
		} else {
			int zx=ix>>(bits-2);
			int zy=iy>>(bits-2);
			int si=getIndex(zx,zy);
			SparseMap<T> submap=(SparseMap<T>)data[si]; 
			if (submap==null) return null;
			return submap.getInternal(ix-(zx<<(bits-2)),iy-(zy<<(bits-2)));
		}
	}
	
	private static int getIndex(int ix, int iy) {
		return ix+(iy<<2);
	}
	
	public SparseMap<T> update(int x, int y, T value) {
		if (value==null) {		
			return clear(x,y);		
		} else {			
			int bo=baseOffset(bits);
			int ix=x-bo;
			int iy=y-bo;
			if ((ix<0)||(iy<0)) return extend().update(x, y,value);
			int size=1<<bits;
			if ((ix>=size)||(iy>=size)) return extend().update(x, y,value);
			return updateNotNullInternal(ix,iy,value);
		}
	}
	
	private SparseMap<T> extend() {
		Object[] data = new Object[16];
		data[5]=this; // consistent with base offset being (-1,-1)
		return new SparseMap<T>(bits+2,data);
	}
	
	@SuppressWarnings("unchecked")
	private SparseMap<T> updateNotNullInternal(int ix, int iy, T value) {
		if (bits==2) {	
			int i=getIndex(ix,iy);
			T current= (T)data[i];
			if (value.equals(current)) return this;
			return update(i,value);
		} else {
			int zx=ix>>(bits-2);
			int zy=iy>>(bits-2);
			int si=getIndex(zx,zy);
			SparseMap<T> submap=(SparseMap<T>)data[si]; 
			if (submap==null) return update(si,createInternal(bits-2,ix-(zx<<(bits-2)),iy-(zy<<(bits-2)),value));
			
			SparseMap<T> newSubmap=submap.updateNotNullInternal(ix-(zx<<(bits-2)),iy-(zy<<(bits-2)), value);
			return newSubmap;
		}		
	}
	
	// creates series -1, -1-4 , -1-4-16 etc.
	public static int baseOffset(int bits) {
		return -1-(((1<<bits)-1) & 0x55555554);
	}
	
	@SuppressWarnings("unchecked")
	private static SparseMap<?> createInternal(int bits, int ix, int iy, Object value) {
		Object[] data=new Object[16];
		if (bits==2) {
			data[getIndex(ix,iy)]=value;
			return new SparseMap(bits,data);
		} else {
			int zx=ix>>(bits-2);
			int zy=iy>>(bits-2);
			int si=getIndex(zx,zy);
			data[si]=createInternal(bits-2,ix-(zx<<(bits-2)),iy-(zy<<(bits-2)),value);
			return new SparseMap(bits,data);
		}
	}

	private SparseMap<T> update(int i, Object val) {
		Object[] newData=data.clone();
		newData[i]=val;
		return new SparseMap<T>(bits,newData);
	}
	
	public SparseMap<T> clear(int x, int y) {
		int bo=baseOffset(bits);
		int ix=x-bo;
		int iy=y-bo;
		if ((ix<0)||(iy<0)) return this;
		int size=1<<bits;
		if ((ix>=size)||(iy>=size)) return this;
		
		return clearInternal(ix,iy);
	}

	@SuppressWarnings("unchecked")
	private SparseMap<T> clearInternal(int ix, int iy) {
		if (bits==2) {	
			int i=getIndex(ix,iy);
			T current= (T)data[i];
			if (current==null) return this;
			return update(i,null);
		} else {
			int zx=ix>>(bits-2);
			int zy=iy>>(bits-2);
			int si=getIndex(zx,zy);
			SparseMap<T> submap=(SparseMap<T>)data[si]; 
			if (submap==null) return this;
			
			SparseMap<T> newSubmap=submap.clearInternal(ix-(zx<<(bits-2)),iy-(zy<<(bits-2)));
			if (submap==newSubmap) return this;
			return update(si,newSubmap);
		}		
	}
}
