package mikera.math;

import mikera.util.Maths;

public class VF {

	public static VectorFunction noiseFunction(final int outputDimensions) {
		return new VectorFunction() {
			@Override public int outputDimensions() {return outputDimensions;}
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				switch (isize) {
					case 1:
						for (int i=0; i<outputDimensions; i++) {
							output.data[i]=PerlinScalar.noise(input.data[0], i);
						}
						return;
					case 2:
						for (int i=0; i<outputDimensions; i++) {
							output.data[i]=PerlinScalar.noise(input.data[0],input.data[1], i);
						}
						return;
					case 3:
						for (int i=0; i<outputDimensions; i++) {
							output.data[i]=PerlinScalar.noise(input.data[0],input.data[1], input.data[2], i);
						}
						return;
					case 4:
						for (int i=0; i<outputDimensions; i++) {
							output.data[i]=PerlinScalar.noise(input.data[0]+2*i,input.data[1]-3*i, input.data[2]+10*i, input.data[3]-17*i);
						}
						return;

				}
				throw new Error("Unsupported noise input dimension: "+isize);
			}
		};
	}
	
	public static VectorFunction clamp(final float min, final float max) {
		return new VectorFunction() {
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				for (int i=0; i<isize; i++) {
					output.data[i]=Maths.clamp(input.data[i], min, max);
				}
			}
		};		
	}
	
	public static VectorFunction takeComponents(final int start, final int count) {
		return new VectorFunction() {
			public void calculate(Vector input, Vector output) {
				for (int i=0; i<count; i++) {
					output.data[i]=input.data[start+i];
				}
			}
		};		
	}
	
	public static VectorFunction zeroExtendComponents(final int inputDimensions, final int outputDimensions) {
		return new VectorFunction() {
			public void calculate(Vector input, Vector output) {
				for (int i=0; i<inputDimensions; i++) {
					output.data[i]=input.data[i];
				}
				for (int i=inputDimensions; i<outputDimensions; i++) {
					output.data[i]=0;
				}
			}
		};		
	}
	
	public static VectorFunction fillComponents(Function<Vector,Vector> f, final int outputDimensions) {
		return new VectorFunction() {
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				for (int i=0; i<outputDimensions; i++) {
					output.data[i]=input.data[i%isize];
				}
			}
		};		
	}
	
	public static VectorFunction add(final Function<Vector,Vector> f1, final Function<Vector,Vector> f2) {
		return new VectorFunction() {
			Vector temp=new Vector(10);
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				if (temp.size()!=isize) temp=new Vector(isize);
				f1.calculate(input, temp);
				f2.calculate(input, output);
				for (int i=0; i<isize; i++) {
					output.data[i]+=temp.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction setComponent(final Function<Vector,Vector> f1, final int component, final Function<Vector,Vector> scalarFunction) {
		return new VectorFunction() {
			Vector temp=new Vector(1);
			public void calculate(Vector input, Vector output) {
				f1.calculate(input, output);
				scalarFunction.calculate(input, temp);
				output.data[component]=temp.data[0];
			}
		};		
	}
	
	public static VectorFunction getComponent(final int component) {
		return new VectorFunction() {
			public void calculate(Vector input, Vector output) {
				output.data[0]=input.data[component];
			}
		};		
	}
	
	public static VectorFunction setComponents(final Function<Vector,Vector> f1, final int component, final int count, final Function<Vector,Vector> vectorFunction) {
		return new VectorFunction() {
			Vector temp=new Vector(count);
			public void calculate(Vector input, Vector output) {
				f1.calculate(input, output);
				vectorFunction.calculate(input, temp);
				for (int i=0; i<count; i++) {
					output.data[component+i]=temp.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction select(final Function<Vector,Vector> f1, final Function<Vector,Vector> f2, final Function<Vector,Vector> f3) {
		return new VectorFunction() {
			public void calculate(Vector input, Vector output) {
				f1.calculate(input, output);
				if (output.data[0]>0) {
					f2.calculate(input, output);				
				} else {
					f3.calculate(input, output);						
				}
			}
		};		
	}
	
	public static VectorFunction madd(final Function<Vector,Vector> f1, final Function<Vector,Vector> f2, final double v) {
		return new VectorFunction() {
			Vector temp=new Vector(10);
			float factor=(float)v;
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				if (temp.size()!=isize) temp=new Vector(isize);
				f2.calculate(input, temp);
				f1.calculate(input, output);
				for (int i=0; i<isize; i++) {
					output.data[i]+=factor*temp.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction add(final Function<Vector,Vector> f1, final Vector v) {
		return new VectorFunction() {
			final Vector value=new Vector(v);
			public void calculate(Vector input, Vector output) {
				int isize=value.size();
				f1.calculate(input, output);
				for (int i=0; i<isize; i++) {
					output.data[i]+=value.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction muliply(final Function<Vector,Vector> f1, final Vector v) {
		return new VectorFunction() {
			final Vector value=new Vector(v);
			public void calculate(Vector input, Vector output) {
				int isize=value.size();
				f1.calculate(input, output);
				for (int i=0; i<isize; i++) {
					output.data[i]*=value.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction muliply(final Function<Vector,Vector> f1, final double v) {
		return new VectorFunction() {
			final float value=(float)v;
			public void calculate(Vector input, Vector output) {
				f1.calculate(input, output);
				int osize=output.size();
				for (int i=0; i<osize; i++) {
					output.data[i]*=value;
				}
			}
		};		
	}
	
	public static VectorFunction compose(final Function<Vector,Vector> outer,final Function<Vector,Vector> inner, final int innerDimensions) {
		return new VectorFunction() {
			Vector temp=new Vector(innerDimensions);
			public void calculate(Vector input, Vector output) {
				inner.calculate(input, temp);
				outer.calculate(temp, output);
			}
		};			
	}
	
	public static VectorFunction constant(final Vector v) {
		return new VectorFunction() {
			final Vector value=new Vector(v);
			public void calculate(Vector input, Vector output) {
				int isize=value.size();
				for (int i=0; i<isize; i++) {
					output.data[i]=value.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction multiply(final Function<Vector,Vector> f1, final Function<Vector,Vector> f2) {
		return new VectorFunction() {
			Vector temp=new Vector(1);
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				if (temp.size()!=isize) temp=new Vector(isize);
				f1.calculate(input, temp);
				f2.calculate(input, output);
				for (int i=0; i<isize; i++) {
					output.data[i]*=temp.data[i];
				}
			}
		};		
	}
	
	public static VectorFunction perturb(final Function<Vector,Vector> f1, final Function<Vector,Vector> f2) {
		return perturb(f1,f2,1);
	}
	
	public static VectorFunction perturb(final Function<Vector,Vector> f1, final Function<Vector,Vector> f2, final double v) {
		return new VectorFunction() {
			final float factor=(float)v;
			Vector temp=new Vector(3);
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				if (temp.size()!=isize) temp=new Vector(isize);
				f2.calculate(input, temp);
				for (int i=0; i<isize; i++) {
					temp.data[i]=temp.data[i]*factor+input.data[i];
				}
				f1.calculate(temp, output);
			}
		};		
	}
	
	public static VectorFunction scale(final Function<Vector,Vector> f1, final Vector v) {
		return new VectorFunction() {
			Vector temp=new Vector(3);
			Vector scaleFactor=new Vector(v);
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				if (temp.size()!=isize) temp=new Vector(isize);
				for (int i=0; i<isize; i++) {
					temp.data[i]=input.data[i]*scaleFactor.data[i];
				}
				f1.calculate(temp, output);
			}
		};		
	}
	
	public static VectorFunction scale(final Function<Vector,Vector> f1, final double v) {
		return new VectorFunction() {
			Vector temp=new Vector(3);
			float factor=(float)v;
			public void calculate(Vector input, Vector output) {
				int isize=input.size();
				if (temp.size()!=isize) temp=new Vector(isize);
				for (int i=0; i<isize; i++) {
					temp.data[i]=input.data[i]*factor;
				}
				f1.calculate(temp, output);
			}
		};		
	}

}
