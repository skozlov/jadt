package com.github.skozlov.jadt.demo;

import com.github.skozlov.jadt.ADT2;

abstract class Person implements ADT2<Person, Person.Bro, Person.NotBro> {
	private Person(){}

	@Override
	public Class<Bro> getSubtype1() {
		return Bro.class;
	}

	@Override
	public Class<NotBro> getSubtype2() {
		return NotBro.class;
	}

	static final class Bro extends Person{
		final String name;

		Bro(String name) {
			this.name = name;
		}

	}

	static final class NotBro extends Person{
	}
}
