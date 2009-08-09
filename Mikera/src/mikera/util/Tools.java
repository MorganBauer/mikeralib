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
	public static int middle(int a, int b, int c) {
		if (a<b) {
			if (b<c) {
				return b;
			}
			return (a<c)?c:a;
		} else {
			if (a<c) {
				return a;
			}
			return (b<c)?c:b;
		}
		
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
	
	public static <T extends Comparable<? super T>> void swap(T[] a, int x, int y) {
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
