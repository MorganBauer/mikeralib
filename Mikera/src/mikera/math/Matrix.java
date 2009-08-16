package mikera.math;

import mikera.util.*;

/**
 * Multi-dimension Matrix
 * 
 * @author Mike
 *
 */
public final class Matrix implements VectorFunction {
	public float[] data;
	private int rows=0;
	private int columns=0;
	
	public Matrix() {
		
	}
	
	public Matrix(int r, int c) {
		data=new float[r*c];
		rows=r;
		columns=c;
	}
	

	
	public Matrix(Matrix a) {
		this(a.data);
	}
	
	public Matrix(float[] adata) {
		int size=adata.length;
		data=new float[size];
		System.arraycopy(adata, 0, data, 0, size);
	}
	
	public Matrix construct(float[] dataToEmbed, int r, int c) {
		Matrix v=new Matrix();
		v.data=dataToEmbed;
		v.rows=r;
		v.columns=c;
		return v;
	}
	
	public int size() {
		return data.length;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public void setToIdentity() {
		for (int y=0;y<rows; y++) {
			for (int x=0; x<columns; x++) {
				data[x+y*columns]=(x==y)?1:0;
			}
		}
	}
	
	public void setToRotation3(Vector u, float angle) {
		setToRotation3(u.data[0],u.data[1],u.data[2],angle);
	}
	
	public void setToRotation3(float ux, float uy, float uz, float angle) {
		float dd=ux*ux+uy*uy+uz*uz;
		if (dd==0.0f) {
			setToIdentity();
			return;
		}
		if (dd!=1.0f) {
			float fac=1.0f/(float)Math.sqrt(dd);
			ux*=fac;
			uy*=fac; 
			uz*=fac;
		}
		
		float c=(float)Math.cos(angle);
		float s=(float)Math.sin(angle);
		
		data[0+0*columns]=ux*ux+(1-ux*ux)*c;
		data[1+0*columns]=ux*uy*(1-c)-uz*s;
		data[2+0*columns]=ux*uz*(1-c)+uy*s;
		data[0+1*columns]=ux*uy*(1-c)+uz*s;
		data[1+1*columns]=uy*uy+(1-uy*uy)*c;
		data[2+1*columns]=uy*uz*(1-c)-ux*s;
		data[0+2*columns]=ux*uz*(1-c)-uy*s;
		data[1+2*columns]=uy*uz*(1-c)+ux*s;
		data[2+2*columns]=uz*uz+(1-uz*uz)*c;
	}
	
	public String toString() {
		String result="{";
		int max=data.length-1;
		for (int i=0; i<max; i++) {
			result+=Float.toString(data[i]);
			result+=", ";
		}
		result+=Float.toString(data[max]);
		return result+"}";
	}
		
	
	public static void multiplyVector(Matrix matrix, Vector vector, Vector target) {
		multiplyVector(matrix.data,0,matrix.rows,matrix.columns,vector.data,0, target.data,0);
	}

	
	public static void multiplyVector(float[] matrix, int o1, int rows, int columns, float[] vector, int o2, float[] target, int o3) {
		for (int y=0; y<rows; y++) {
			float val=0.0f;
			for (int x=0; x<columns; x++) {
				val+=matrix[o1+x+y*columns]*vector[o2+x];
			}
			target[o3+y]=val;
		}
	}

	public void calculate(Vector input, Vector output) {
		multiplyVector(data,0,rows,columns,input.data,0,output.data,0);
	}

	public int inputDimensions() {
		return columns;
	}

	public int outputDimensions() {
		// TODO Auto-generated method stub
		return rows;
	}
}
