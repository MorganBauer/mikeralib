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
	
	public static class DefaultComparator<T> implements Comparator<T>, Serializable {
		private static final long serialVersionUID = 1695713461396657889L;

		@SuppressWarnings("unchecked")
		public int compare(T o1, T o2) {
			return ((Comparable<T>)o1).compareTo(o2);
		}
	}
	
	public static int compareWithNulls(Object a, Object b) {
		if (a==b) {
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
	 * Hash code based on summed hash codes of individual integer values
	 * 
	 * Defined as XOR of hashcodes of all elements rotated right for each element, to be consistent with PersistentList<T>
	 * 
	 * @param data
	 * @return
	 */
	public static int hashCode(int[] data) {
		int result=0;
		for(int i=0; i<data.length; i++) {
			result^=hashCode(data[i]);
			result=Integer.rotateRight(result, 1);
		}
		return result;
	}
	
	public static<T> int hashCode(Iterator<T> data) {
		int result=0;
		
		while(data.hasNext()) {
			result^=hashCodeWithNulls(data.next());
			result=Integer.rotateRight(result, 1);
		}
		return result;
	}
	
	/**
	 * Hashcode for an int, defined as the value of the int itself for consistency with java.lang.Integer
	 * 
	 * @param value
	 * @return
	 */
	public static int hashCode(int value) {
		return value;
	}
	
	public static int hashCodeWithNulls(Object value) {
		if (value==null) return 0;
		return value.hashCode();
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
		if (a==b) return 0;
		if (a==null) return -1;
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
}
