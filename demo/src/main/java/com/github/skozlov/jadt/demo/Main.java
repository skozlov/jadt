package com.github.skozlov.jadt.demo;

public class Main {
	public static void main(String[] args) {
		sayHello(new Person.Bro("John"));// prints "Hi John!"
		sayHello(new Person.NotBro());//prints "You're not my bro."
	}

	private static void sayHello(Person person){
		person.forEach(
			bro -> {
				System.out.printf("Hi %s!", bro.name);
				System.out.println();
			},
			notBro -> System.out.println("You're not my bro.")
		);
	}
}
