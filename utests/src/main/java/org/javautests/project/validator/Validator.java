package org.javautests.project.validator;

import java.util.Collection;
import java.util.function.Predicate;

public interface Validator<T> {

	public boolean add(Predicate<T> validator);
	
	public void clear();
	
	public Collection<Predicate<T>> getAllValidators();
	
	public boolean validate(T value);
	
}
