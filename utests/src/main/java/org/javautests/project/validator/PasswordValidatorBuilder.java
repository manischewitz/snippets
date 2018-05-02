package org.javautests.project.validator;

import java.util.function.Predicate;
/**
 * One object per builder, so subsequent calls to build() will be producing brand new object reference.
 * NOT thread-safe.
 * */
public final class PasswordValidatorBuilder {

	private Validator<Character[]> validator;
	
	public PasswordValidatorBuilder() {
		this.validator = new GenericValidator<>();
	}
	
	public final PasswordValidatorBuilder length(int min, int max) {
		if (min < 0 || max < 0 || max - min < 0) {
			throw new IllegalArgumentException();
		}
		validator.add((Character[] value) -> {
			return value.length >= min && value.length <= max;
		});
		return this;
	}
	
	public final PasswordValidatorBuilder haveAtLeastXDigits(int min){
		if (min < 0) {
			throw new IllegalArgumentException();
		}
		validator.add((Character[] values) -> {
			int digitCounter = 0;
			for (Character c : values) {
				if (Character.isDigit(c)) {
					digitCounter++;
				}
			}
			return digitCounter >= min; 
		});
		return this;
	}
	
	public final PasswordValidatorBuilder containSymbol(Character symbol, int minTimes) {
		validator.add((Character[] values) -> {
			int counter = 0;
			for (Character c : values) {
				if (c.equals(symbol)) {
					counter++;
				}
			}
			return counter >= minTimes; 
		});
		return this;
	}
	
	public final PasswordValidatorBuilder ContainMixtureOfLowerAndCapitalLetters() {
		validator.add((Character[] values) -> {
			boolean capital = false;
			boolean lower = false;
			for (Character c : values) {
				if (Character.isLowerCase(c)) {
					lower = true;
				}
				if (Character.isUpperCase(c)) {
					capital = true;
				}
			}
			return capital && lower; 
		});
		return this;
	}
	
	public final Validator<Character[]> build() {
		Validator<Character[]> temp = validator;
		validator = new GenericValidator<>();
		return temp;
	}
	
}
