package mikera.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import mikera.util.emptyobjects.NullArrays;

public class Arrays {
	public static final float[] NULL_FLOATS=NullArrays.NULL_FLOATS;
	public static final int[] NULL_INTS=NullArrays.NULL_INTS;
	public static final byte[] NULL_BYTES=NullArrays.NULL_BYTES;
	public static final double[] NULL_DOUBLES=NullArrays.NULL_DOUBLES;
	
	public static <T extends Comparable<? super T>> boolean isSorted(T[] a, int start, int end) {
		while (start<end) {
			if (a[start].compareTo(a[start+1])>0) return false;
			start++;
		}
		return true;
	}
	
	public static <T extends Comparable<? super T>> boolean isSorted(List<T> a) {
		int length=a.size();
		if (length<=1) return true;
		
		int i=1;
		T previous=a.get(0);
		while (i<length) {
			T current=a.get(i++);
			if (previous.compareTo(current)>0) return false;
			previous=current;
		}
		return true;
	}
	
	public static <T> T[] insertElement(T[] array, T value, int i) {
		int count=array.length;
		@SuppressWarnings("unchecked")
		T[] newarray=(T[]) Array.newInstance(array.getClass().getComponentType(), count+1);
		
		System.arraycopy(array, 0, newarray, 0, i);
		newarray[i]=value;
		System.arraycopy(array, i, newarray, i+1, count-i);

		return newarray;
	}
	

	public static float[] insertElement(float[] array, float value, int i) {
		int count=array.length;
		float[] newarray=new float[count+1];
		
		System.arraycopy(array, 0, newarray, 0, i);
		newarray[i]=value;
		System.arraycopy(array, i, newarray, i+1, count-i);

		return newarray;
	}
	
	public static <T> T[] deleteElement(T[] array, int i) {
		int count=array.length;
		@SuppressWarnings("unchecked")
		T[] newarray=(T[]) Array.newInstance(array.getClass().getComponentType(), count-1);
		
		System.arraycopy(array, 0, newarray, 0, i);
		System.arraycopy(array, i+1, newarray, i, count-i-1);

		return newarray;
	}
	
	public static float[] deleteElement(float[] array, int i) {
		int count=array.length;
		float[] newarray=new float[count-1];;
		
		System.arraycopy(array, 0, newarray, 0, i);
		System.arraycopy(array, i+1, newarray, i, count-i-1);

		return newarray;
	}
	
	public static void swap(int[] data, int a, int b) {
		int t=data[a];
		data[a]=data[b];
		data[b]=t;
	}
	
	public static int[] deduplicate(int[] sortedData) {
		int di=0;
		int si=1;
		while (si<sortedData.length) {
			int v=sortedData[si];
			if (sortedData[di]==v) {
				si++;
			} else {
				sortedData[di+1]=v;
				di++;
				si++;
			}
		}
		di++;
		if (di<sortedData.length) {
			int[] ndata=new int[di];
			System.arraycopy(sortedData, 0, ndata, 0, di);
			return ndata;
		} else {
			return sortedData;
		}
	}
	
	public static float squareDistance(float[] a, float[] b) {
		float eSquared=0;
		for (int i=0; (i<a.length); i++) {
			float d=a[i]-b[i];
			eSquared+=d*d;
		}
		return eSquared;
	}
	
	public static void fillRandom(float[] a) {
		fillRandom(a,0,a.length);
	}
	
	public static void fillRandom(float[] a, int start, int length) {
		for (int i=0; i<length; i++) {
			a[start+i]=Rand.nextFloat();
		}
	}

	public static <T> void swap(List<T> a, int x, int y) {
		T t=a.get(x);
		a.set(x,a.get(y));
		a.set(y,t);
	}
	
	public static <T> void swap(ArrayList<T> a, int x, int y) {
		T t=a.get(x);
		a.set(x,a.get(y));
		a.set(y,t);
	}
	
	public static <T> void swap(T[] a, int x, int y) {
		T t=a[x];
		a[x]=a[y];
		a[y]=t;
	}


	public static <T extends Comparable<? super T>> void mergeInOrder(T[] src, T[] dst, int p1, int p2, int p3, int p4) {
		if (src[p2].compareTo(src[p3])<=0) return; // already sorted!
		
		// cut away ends
		while (src[p1].compareTo(src[p3])<=0) p1++;
		while (src[p2].compareTo(src[p4])<=0) p4--;
		
		int i1=p1;
		int i3=p3;
		int di=p1;
		while(di<p4) {
			if (src[i1].compareTo(src[i3])<=0) {
				dst[di++]=src[i1++];
			} else {
				dst[di++]=src[i3++];
				if (i3>p4) {
					System.arraycopy(src,i1,dst,di,p2-i1+1);
					break;
				}
			}
		}
		
		System.arraycopy(dst, p1, src, p1, (p4-p1)+1);
	}

	public static <T extends Comparable<? super T>> void mergeSort(T[] src, T[] dst, int start, int end) {
		if (start+1>=end) {
			if (start>=end) return;
			if (src[start].compareTo(src[end])>0) {
				swap(src,start,end);
			}
			return;
		}
		
		int middle=(start+end)/2;
		mergeSort(src,dst,start, middle);
		mergeSort(src,dst,middle+1, end);
		mergeInOrder(src,dst,start,middle,middle+1,end);
	}
	
	private static ThreadLocal<Comparable<?>[]> mergeSortTemp=new ThreadLocal<Comparable<?>[]>();
	
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> void mergeSort(T[] src) {
		int length=src.length;
		Comparable<?>[] temp=mergeSortTemp.get();
		if ((temp==null)||(temp.length<length)) {
			temp=new Comparable[length*3/2];
			mergeSortTemp.set(temp);
		}
		mergeSort(src,(T[])temp,0,length-1);
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> al=new ArrayList<Integer>();
		System.out.println(Arrays.isSorted(al));
		al.add(1);
		System.out.println(Arrays.isSorted(al));
		al.add(2);
		System.out.println(Arrays.isSorted(al));
		al.add(10);
		System.out.println(Arrays.isSorted(al));
		al.add(3);
		System.out.println(Arrays.isSorted(al));	
	}

	public static <T> T[] resize(T[] array, int newsize) {
		int len=array.length;
		@SuppressWarnings("unchecked")
		T[] newarray=(T[]) Array.newInstance(array.getClass().getComponentType(), newsize);
		System.arraycopy(array, 0, newarray, 0, Maths.min(newsize,len));
		return newarray;
	}

}
