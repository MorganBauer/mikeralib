package mikera.engine;

import mikera.util.*;

/**
 * Multi-dimension vector class
 * 
 * @author Mike
 *
 */
public final class Vector {
	public float[] data;
	
	public Vector() {
		
	}
	
	public Vector(int size) {
		data=new float[size];
	}
	
	public Vector(float x, float y) {
		this(2);
		data[0]=x;
		data[1]=y;
	}
	
	public Vector(int x, int y, int z) {
		this(3);
		data[0]=x;
		data[1]=y;
		data[2]=z;
	}
	
	public Vector(Vector a) {
		this(a.data);
	}
	
	public Vector(float[] adata) {
		int size=adata.length;
		data=new float[size];
		System.arraycopy(adata, 0, data, 0, size);
	}
	
	public Vector construct(float[] dataToEmbed) {
		Vector v=new Vector();
		v.data=dataToEmbed;
		return v;
	}
	
	public void resize(int size) {
		if (size!=data.length) {
			float[] newData=new float[size];
			System.arraycopy(data,0,newData,0,Math.min(size,data.length));
			data=newData;
		}
	}
	
	public int size() {
		return data.length;
	}
	
	public Vector(float x, float y, float z) {
		this(3);
		data[0]=x;
		data[1]=y;
		data[2]=y;
	}
	
	public void add(Vector v) {
		for (int i=0; i<data.length; i++) {
			data[i]+=v.data[i];
		}
	}

	public void addMultiple(Vector v, float factor) {
		for (int i=0; i<data.length; i++) {
			data[i]+=v.data[i]*factor;
		}
	}
	
	public void set(int index, float value) {
		data[index]=value;
	}
	
	public void scale(float f) {
		for (int i=0; i<data.length; i++) {
			data[i]*=f;
		}
	}
	
	public float dot(Vector v) {
		float result=0;
		for (int i=0; i<data.length; i++) {
			result+=data[i]*v.data[i];
		}
		return result;
	}
	
	public float lengthSquared() {
		float result=0;
		for (int i=0; i<data.length; i++) {
			result+=data[i]*data[i];
		}
		return result;		
	}
	
	public float length() {
		return Maths.sqrt(lengthSquared());
	}
	
	public void cross(Vector v) {
		float x=data[1]*v.data[2]-data[2]*v.data[1];
		float y=data[2]*v.data[0]-data[0]*v.data[2];
		float z=data[0]*v.data[1]-data[1]*v.data[0];
		data[0]=x;
		data[1]=y;
		data[2]=z;
	}
	
	public Vector cross(Vector a, Vector b) {
		float x=a.data[1]*b.data[2]-a.data[2]*b.data[1];
		float y=a.data[2]*b.data[0]-a.data[0]*b.data[2];
		float z=a.data[0]*b.data[1]-a.data[1]*b.data[0];	
		return new Vector(x,y,z);
	}
}
