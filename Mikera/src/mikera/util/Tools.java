package mikera.util;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult;

public class Tools {
	
	public static class HashComparator<T> implements Comparator<T>, Serializable {
		private static final long serialVersionUID = -568440287836864164L;

		public int compare(T o1, T o2) {
			return o2.hashCode()-o1.hashCode();
		}
		
	}
	
	public static int compareWithNulls(Object a, Object b) {
		if ((a==null)&&(b==null)) {
			return 0;
		}
		throw new Error("Objects not comparable unless they implement the Comparable interface!");
	}
	
	public static boolean equalsWithNulls(Object a, Object b) {
		if (a==b) return true;
		if ((a==null)||(b==null)) return false;
		return a.equals(b);
	}
	
	public static <T> ArrayList<T> buildArrayList(Iterator<T> iterator) {
		ArrayList<T> al=new ArrayList<T>();
		while (iterator.hasNext()) {
			al.add(iterator.next());
		}
		return al;
	}
	
	public static <T> HashSet<T> buildHashSet(Iterator<T> iterator) {
		HashSet<T> hs=new HashSet<T>();
		while (iterator.hasNext()) {
			hs.add(iterator.next());
		}
		return hs;
	}
	
	/**
	 * Compares two Comparable values, considering null as the lowest possible value 
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> int compareWithNulls(T a, T b) {
		if (a==null) {
			if (b==null) {
				return 0;
			} else {
				return -1;
			}
		}
		if (b==null) return 1;
		return a.compareTo(b);
	}
	
	public static void writeXMLToFile(Document doc, String fileName) {
		try {
			TransformerFactory factory=TransformerFactory.newInstance();
			factory.setAttribute("indent-number", 4);
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			Source source = new DOMSource(doc);
			Result result = new StreamResult(new OutputStreamWriter(new FileOutputStream(new File(fileName)),"UTF-8"));
			transformer.transform(source, result);
		} catch (Throwable t) {
			throw new Error(t);
		}
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
}
