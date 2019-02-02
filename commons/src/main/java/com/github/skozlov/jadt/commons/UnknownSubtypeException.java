package com.github.skozlov.jadt.commons;

/**
 * Indicates that the type of the instance of an ADT is not registered as an ADT subtype.
 */
public class UnknownSubtypeException extends RuntimeException {
	/**
	 * @param type type of the instance of an ADT
	 */
	public UnknownSubtypeException(Class<?> type){
		super(String.format("Unknown subtype %s", type));
	}
}
