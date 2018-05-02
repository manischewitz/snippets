package org.javautests.project.validator;

import static org.junit.Assert.*;

import javax.xml.stream.events.Characters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class PasswordValidatorBuilderTest {

	private static final Object[] getLegalParams() {
		return new Object[] { 
				new Object[] {0, 6, new Character[] {'j', 'U', 'n', 'i', 't'}},
				new Object[] {4, 4, new Character[] {'t', 'e', 's', 't'}},
				new Object[] {1, 2, new Character[] {'j'}}
		};
	}
	
	
	private static final Object[] getIllegalParams() {
		return new Object[] { 
				new Object[] {0, 3, new Character[] {'j', 'U', 'n', 'i', 't'}},
				new Object[] {4, 4, new Character[] {'t'}},
				new Object[] {1, 2, new Character[] {}}
		};
	}
	
	@Test
	@Parameters(method = "getLegalParams")
	public void shouldAllowNCharsLong(int min, int max, Character[] password) {
		Validator<Character[]> validator = new PasswordValidatorBuilder().length(min, max).build();
		assertTrue(validator.validate(password));
	}
	
	@Test
	@Parameters(method = "getIllegalParams")
	public void shouldNotAllowLessThanNCharsLong(int min, int max, Character[] password) {
		Validator<Character[]> validator = new PasswordValidatorBuilder().length(min, max).build();
		assertFalse(validator.validate(password));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowMaxLessThanMinInPassword() {
		 new PasswordValidatorBuilder().length(6, 3).build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowLessThanZeroValuesInPassword() {
		 new PasswordValidatorBuilder().length(-6, 3).build();
	}
	
	@Test
	public void shouldHaveAtLeastXDigits() {
		Validator<Character[]> validator = new PasswordValidatorBuilder()
				.haveAtLeastXDigits(3).length(0, 10).build();
		assertTrue(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't', '2', '5', '6', '4'}));
		assertFalse(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't', '2', '5'}));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowLessThanZeroValuesInHaveAtLeastXDigits() {
		new PasswordValidatorBuilder().haveAtLeastXDigits(-3).build();
	}
	
	@Test
	public void shouldContainUnderscore() {
		Validator<Character[]> validator = new PasswordValidatorBuilder().containSymbol('_', 1).build();
		assertTrue(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't', '_', '2', '5', '6', '4'}));
		assertFalse(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't', '2', '5'}));
		
		validator = new PasswordValidatorBuilder().containSymbol('7', 3).build();
		assertTrue(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't', '_', '7', '7', '7', '4'}));
		assertFalse(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't', '7', '7'}));
	}
	
	@Test
	public void shouldContainMixtureOfLowerAndCapitalLetters() {
		Validator<Character[]> validator = new PasswordValidatorBuilder()
				.ContainMixtureOfLowerAndCapitalLetters().build();
		assertTrue(validator.validate(new Character[] {'j', 'U', 'n', 'i', 't'}));
		assertFalse(validator.validate(new Character[] {'j', 'u', 'n', 'i', 't'}));
	}
	
	@Test
	public void shouldVerifyFullBlownPassword() {
		Validator<Character[]> validator = new PasswordValidatorBuilder()
				.ContainMixtureOfLowerAndCapitalLetters()
				.containSymbol('_', 2)
				.containSymbol('.', 1)
				.haveAtLeastXDigits(4)
				.length(0, 16)
				.build();
		assertTrue(validator.validate(new Character[] {'j', 'u', 'n', 'i', 't', '_', '1', '5', '6', '_', '1', '.', 'S'}));
		assertFalse(validator.validate(new Character[] {'j', 'u', 'n', 'i', 't', '_', '1', '5', '6', '_', '1', '.', 's'}));
	}
	
}
