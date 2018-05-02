package org.javautests.project.validator;

import static org.junit.Assert.*;

import java.util.function.Predicate;

import org.junit.Test;

public class GenericValidatorTest {

	private GenericValidator<Integer> v = new GenericValidator<>();
	private static final Predicate<Integer> GREATERTHAN100 = (Integer val) -> {
		return val > 100;
	};
	
	@Test 
	public void shouldAddValidators() {
		assertEquals(0, v.getAllValidators().size());
		assertTrue(v.add(v -> false));
		assertEquals(1, v.getAllValidators().size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAddValidators() {
		v.add(null);
	}
	
	@Test 
	public void shouldFailValidate() {
		v.add(GREATERTHAN100 );
		assertFalse(v.validate(99));
	}
	
	@Test 
	public void shouldSuccessfullyValidate() {
		v.add(GREATERTHAN100 );
		assertTrue(v.validate(999));
	}
	
	@Test 
	public void shouldClearAllValidators() {
		assertEquals(0, v.getAllValidators().size());
		v.add(GREATERTHAN100 );
		v.add(GREATERTHAN100 );
		assertEquals(2, v.getAllValidators().size());
		v.clear();
		assertEquals(0, v.getAllValidators().size());
	}
	
}
