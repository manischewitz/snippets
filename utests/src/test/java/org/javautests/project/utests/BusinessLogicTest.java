package org.javautests.project.utests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
@SuppressWarnings({ "serial", "unused" })
public class BusinessLogicTest {

	private static final Object[] getLegalParams() {
		return new Object[] { 
				new Object[] {"", ""},
				new Object[] {".", "."},
				new Object[] {"gmo", "omg"}
		};
	}
	
	@Test
	@Parameters(method = "getLegalParams")
	public void shouldHandleOrdinaryString(String expected, String input) {
		assertEquals(expected, BusinessLogic.reverse(input));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnIllegalValuesPassed() {
		BusinessLogic.reverse(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnIllgalArgument() {
		BusinessLogic.fetchAllNumbersWithGivenDigitsCountFromString("asd", -1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnIllgalNullArgument() {
		BusinessLogic.fetchAllNumbersWithGivenDigitsCountFromString(null, 1);
	}
	
	
	private static final Object[] getLegalParamsForDigistInString() {
		return new Object[] { 
				new Object[] {"", 3, new ArrayList<Integer>()},
				new Object[] {"ad 132 adaw acwd32324 224awcd27", 3, new ArrayList<Integer>() {
					{
						add(132);
						add(32324);
						add(224);
					}
				}},
				new Object[] {"323248", 5, new ArrayList<Integer>() {
					{
						add(323248);
					}
				}}
		};
	}
	
	@Test
	@Parameters(method = "getLegalParamsForDigistInString")
	public void shouldFetchNumbersFromString(String string, int digits, List<Integer> expected) {
		List<Integer> result = BusinessLogic.
				fetchAllNumbersWithGivenDigitsCountFromString(string, digits);
		assertTrue(expected.equals(result));
	}
	
}
