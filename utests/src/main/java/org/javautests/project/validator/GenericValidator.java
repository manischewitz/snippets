package org.javautests.project.validator;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
/**
 * @author macuser
 * */
public class GenericValidator<T> implements Validator<T> {

	private final Collection<Predicate<T>> validators;
	
	public GenericValidator() {
		validators = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public boolean add(Predicate<T> validator) {
		if (validator == null) {
			throw new IllegalArgumentException();
		}
		return validators.add(validator);
	}

	@Override
	public void clear() {
		validators.clear();
	}

	@Override
	public Collection<Predicate<T>> getAllValidators() {
		return Collections.unmodifiableCollection(validators);
	}

	@Override
	public boolean validate(T value) {
		for (Predicate<T> validator : validators) {
			if (!validator.test(value)) {
				return false;
			}
		}
		return true;
	}

	
}
