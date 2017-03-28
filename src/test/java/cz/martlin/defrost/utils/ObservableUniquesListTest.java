package cz.martlin.defrost.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import cz.martlin.defrost.utils.ObservableUniquesList;

public class ObservableUniquesListTest {

	@Test
	public void test() {
		ObservableUniquesList<String> list = new ObservableUniquesList<>();

		assertEquals(0, list.size());

		// adding
		list.add("foo");
		assertEquals(1, list.size());

		list.add("bar");
		assertEquals(2, list.size());

		list.add("baz");
		assertEquals(3, list.size());

		list.add("foo");
		assertEquals(3, list.size());

		// containing
		assertTrue(list.contains("foo"));
		assertTrue(list.contains("bar"));
		assertTrue(list.contains("baz"));
		assertFalse(list.contains("aux"));

		// removing
		list.remove("foo");
		assertEquals(2, list.size());

		list.remove("foo");
		assertEquals(2, list.size());

		list.remove("baz");
		assertEquals(1, list.size());

		// replacing
		list.set(0, "lorem");
		assertEquals(1, list.size());
		assertFalse(list.contains("bar"));
		assertTrue(list.contains("lorem"));

	}

}
