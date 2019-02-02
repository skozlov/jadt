package com.github.skozlov.jadt;

import com.github.skozlov.jadt.Person.Unknown;
import com.github.skozlov.jadt.commons.UnknownSubtypeException;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static com.github.skozlov.jadt.Person.Bro;
import static com.github.skozlov.jadt.Person.NotBro;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ADT2Test {
	@Test
	void mapBro(){
		assertEquals(
			Optional.of("John"),
			new Bro("John").map(bro -> Optional.of(bro.name),  notBro -> Optional.empty())
		);
	}

	@Test
	void mapNotBro(){
		assertEquals(Optional.empty(), new NotBro().map(bro1 -> Optional.of(bro1.name), notBro -> Optional.empty()));
	}

	@Test
	void mapUnknown(){
		Unknown unknown = new Unknown();
		assertThrows(UnknownSubtypeException.class, () -> unknown.map(Function.identity(), Function.identity()));
	}

	@Test
	void forEachBro(){
		MutableBoolean executed = new MutableBoolean(false);
		new Bro("John").forEach(
			bro -> {
				assertEquals("John", bro.name);
				executed.setValue(true);
			},
			notBro -> fail()
		);
		assertTrue(executed.booleanValue());
	}

	@Test
	void forEachNotBro(){
		MutableBoolean executed = new MutableBoolean(false);
		new NotBro().forEach(bro -> fail(), notBro -> executed.setValue(true));
		assertTrue(executed.booleanValue());
	}

	@Test
	void forEachUnknown(){
		Unknown unknown = new Unknown();
		assertThrows(UnknownSubtypeException.class, () -> unknown.forEach(bro -> fail(), notBro -> fail()));
	}

	@Test
	void ifSubtype1Bro(){
		MutableBoolean executed = new MutableBoolean(false);
		new Bro("John").ifSubtype1(bro -> {
			assertEquals("John", bro.name);
			executed.setValue(true);
		});
		assertTrue(executed.booleanValue());
	}

	@Test
	void ifSubtype1NotBro(){
		new NotBro().ifSubtype1(notBro -> fail());
	}

	@Test
	void ifSubtype1Unknown(){
		Unknown unknown = new Unknown();
		assertThrows(UnknownSubtypeException.class, () -> unknown.ifSubtype1(u -> fail()));
	}

	@Test
	void ifSubtype2Bro(){
		new Bro("John").ifSubtype2(bro -> fail());
	}

	@Test
	void ifSubtype2NotBro(){
		MutableBoolean executed = new MutableBoolean(false);
		new NotBro().ifSubtype2(notBro -> executed.setValue(true));
		assertTrue(executed.booleanValue());
	}

	@Test
	void ifSubtype2Unknown(){
		Unknown unknown = new Unknown();
		assertThrows(UnknownSubtypeException.class, () -> unknown.ifSubtype2(u -> fail()));
	}
}
