# jADT

Basic ADT interfaces for Java.

## Usage

Add maven dependency:

```xml
<dependency>
    <groupId>com.github.skozlov</groupId>
    <artifactId>jadt</artifactId>
    <version>0.1.0</version>
</dependency>
```

Then implement your ADT:

```java
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
```

Enjoy using it:

```java
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
```