package com.github.skozlov.jadt.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Makes {@link ADTProcessor} generating basic ADT interfaces.
 * It will generate an interface for each number of subtypes
 * from {@link ADT#subtypeNumberMin()} to {@link ADT#subtypeNumberMax()} inclusively.
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface ADT {
	/**
	 * @see #package_()
	 */
	String CURRENT_PACKAGE = "Current package";

	/**
	 * Number of subtypes of the first generated interface.
	 * Must be at least {@code 1}.
	 * Default is {@code 2}.
	 */
	int subtypeNumberMin() default 2;

	/**
	 * Number of subtypes of the last generated interface.
	 * Must be greater than or equal to {@link #subtypeNumberMin()}.
	 */
	int subtypeNumberMax();

	/**
	 * Full name of the package to generate interfaces in.
	 * For an empty string, interfaces will be in no package.
	 * If the {@link #CURRENT_PACKAGE} magic constant specified (default),
	 * interfaces will be generated in the package of the type annotated with this annotation.
	 */
	String package_() default CURRENT_PACKAGE;

	/**
	 * Format of names of generated interfaces.
	 * A name is generated using {@link String#format(String, Object...)},
	 * where the 1st argument is this string, and the 2nd one is number of subtypes of the ADT.
	 * Default is {@code "ADT%d"}, which leads to interface names like {@code ADT2}, {@code ADT3}, etc.
	 */
	String classNameFormat() default "ADT%d";
}
