package com.github.skozlov.jadt.commons;

import java.util.Map;
import java.util.function.Function;

/**
 * For internal usage.
 */
public class ADTUtilsInternal {
	@SafeVarargs
	public static <S, R> R map(S source, Map.Entry<Class<? extends S>, Function<? super S, ? extends R>>... mappers){
		Class<?> sourceClass = source.getClass();
		for (Map.Entry<Class<? extends S>, Function<? super S, ? extends R>> mapper : mappers){
			if (mapper.getKey().isAssignableFrom(sourceClass)){
				return mapper.getValue().apply(source);
			}
		}
		throw new UnknownSubtypeException(sourceClass);
	}
}
