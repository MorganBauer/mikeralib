package mikera.ai;

import mikera.persistent.*;

/**
 * Generic generator functions for tasks
 * 
 * Tasks should be persistent and immutable
 * to allow concurrent sharing by actors
 * 
 * @author Mike Anderson
 *
 */
public class Tasks {
	public static <T,P,R> Task<T,P,R>  select(
			final PersistentList<Task<T,P,R>> ts) 
	{
		return new Task<T,P,R>() {

			@Override
			public R run(T actor, P param) {
				int n=ts.size();
				for (int i=0; i<n; i++) {
					R result=ts.get(i).run(actor, param);
					if (result!=null) return result;
				}
				return null;
			}
			
		};
	}
	
	public static <T,P,R> Task<T,P,R>  select(
			final Task<T,P,R> a, Task<T,P,R> b) {
		PersistentList<Task<T,P,R>> ts=ListFactory.create(a,b);
		return select(ts);
	}
	
	public static <T,P,R> Task<T,P,R>  sequence(
			final PersistentList<Task<T,P,R>> ts) 
	{
		return new Task<T,P,R>() {
			@Override
			public R run(T actor, P param) {
				int n=ts.size();
				R result=null;
				for (int i=0; i<n; i++) {
					result=ts.get(i).run(actor, param);
					if (result==null) return null;
				}
				return result;
			}
		};
	}
	
	public static <T,P,R> Task<T,P,R>  sequence(
			final Task<T,P,R> a, Task<T,P,R> b) {
		PersistentList<Task<T,P,R>> ts=ListFactory.create(a,b);
		return sequence(ts);
	}
	
	public static <T,PR> Task<T,PR,PR> chain(
			final PersistentList<Task<T,PR,PR>> ts) 
	{
		return new Task<T,PR,PR>() {

			@Override
			public PR run(T actor, PR param) {
				int n=ts.size();
				for (int i=0; i<n; i++) {
					param=ts.get(i).run(actor, param);
				}
				return param;
			}
		};
	}
	
	public static <T,P,R1,R2> Task<T,P,R2> test(
			final Task<T,P,R1> cond,
			final Task<T,P,R2> a,
			final Task<T,P,R2> b) 
	{
		return new Task<T,P,R2>() {
			@Override
			public R2 run(T actor, P param) {
				R1 test=cond.run(actor, param);
				if (test!=null) {
					if (a==null) return null;
					return a.run(actor, param);
				} else {
					if (b==null) return null;
					return b.run(actor, param);
				}
			}
		};
	}
	

}
