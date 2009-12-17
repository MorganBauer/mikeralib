package mikera.ai;

import mikera.annotations.Immutable;

/**
 * Generic AI task representation
 * 
 * Intended to be parameterised by actor and 
 * a task-specific parameter
 * 
 * Generate through Tasks.* static methods
 * 
 * @author Mike Anderson
 *
 * @param <T>
 * @param <P>
 * @param <R>
 */

@Immutable
public abstract class Task<T,P,R> {
	public abstract R run(T actor, P param);
}
