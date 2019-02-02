package com.github.skozlov.jadt.commons;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Some useful utilities for lambdas.
 */
public class FunctionUtils {
	/**
	 * @param consumer action to execute in the {@link Function}
	 * @param <T> argument type of the {@link Function}
	 * @return {@link Function} that executes the {@link Consumer} and returns {@code null}
	 */
	public static <T> Function<T, Void> toFunction(Consumer<T> consumer){
		return arg -> {
			consumer.accept(arg);
			return null;
		};
	}
}
