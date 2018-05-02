package org.javautests.project.booking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface BookingSystem<T> {

	public Collection<T> getEntities();

	public T addEntity(T entity, String identifier);

	public List<T> getAllAvailableEntities(LocalDateTime from, LocalDateTime until);

	public boolean isBooked(String identifier, LocalDateTime from, LocalDateTime until);

	/**
	 * Books by first occurrence. For instance (Classroom c) -> {
	 * 		return c.hasProjector() && c.capacity() > 2
	 * };
	 * */
	public boolean book(Predicate<T> predicate, LocalDateTime from, LocalDateTime until);

	public boolean book(String identifier, LocalDateTime from, LocalDateTime until);
	
	
}
