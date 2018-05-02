package org.javautests.project.utests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class HashMapTest {

	private Map<Integer, String> map;
	private static final String FIRST_ARGUMENT = "John Doe";
	private static final String SECOND_ARGUMENT = "Anna Lee";
	private static final String REPLACABLE = "Hillary Trump";
	
	@Before
	public void setUp() {
		map = new HashMap<>();
		map.put(1, FIRST_ARGUMENT);
		map.put(2, SECOND_ARGUMENT);
	}
	
	@Test
	public void shouldRetrieveAddedObjectWithGet() {
		assertSame(FIRST_ARGUMENT, map.get(1));
		assertEquals(FIRST_ARGUMENT, map.get(1));
	}
	
	@Test
	public void shouldReplaceOldObjectWithTheSameKey() {
		assertNotSame(REPLACABLE, map.get(1));
		assertNotEquals(REPLACABLE, map.get(1));
		map.put(1, REPLACABLE);
		assertSame(REPLACABLE, map.get(1));
		assertEquals(REPLACABLE, map.get(1));
	}
	
	@Test
	public void clearShouldRemoveAllContent() {
		assertNotEquals(0, map.size());
		map.clear();
		assertEquals(0, map.size());
	}
	
	@Test
	public void nullCanBeUserAsKey() {
		assertEquals(2, map.size());
		map.put(null, SECOND_ARGUMENT);
		assertEquals(3, map.size());
	}
}
