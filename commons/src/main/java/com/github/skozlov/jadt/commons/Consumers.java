package com.github.skozlov.jadt.commons;

import java.util.function.Consumer;

/**
 * Some useful consumers.
 */
public class Consumers {
	/**
	 * @return consumer that does nothing
	 */
	public static <T> Consumer<T> nop(){
		return arg -> {};
	}
}
