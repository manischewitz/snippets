package org.javautests.project.booking;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.javautests.project.race.Logging;

public class BookingSystemImpl<T> implements BookingSystem<T> {

	private Map<String, Entity> list;
	private Logging log;
	
	private class Entity {
		T entity;
		List<PeriodTuple> bookedOn = new ArrayList<>();
	}
	
	private class PeriodTuple {
		
		LocalDateTime from;
		LocalDateTime until;
		
		public PeriodTuple(LocalDateTime from2, LocalDateTime until2) {
			this.from = from2;
			this.until = until2;
		}
		
	} 
	
	public BookingSystemImpl() {
		this.list = new HashMap<>();
		this.log = new Logging() {

			@Override
			public void log(Object message) {
				System.out.println(message.toString());
				
			}
			
		};
	}
	
	public BookingSystemImpl(Logging log) {
		this.list = new HashMap<>();
		this.log = log;
	}
	

	@Override
	public Collection<T> getEntities() {
		return list.values().stream().map(e -> e.entity).collect(Collectors.toList());
	}

	@Override
	public T addEntity(T entity, String identifier) {
		if (entity == null || identifier == null || identifier.isEmpty()) {
			return null;
		}
		Entity e = new Entity();
		e.entity = entity;
		Entity old = list.put(identifier, e);
		return (old == null) ? entity : old.entity;
	}

	@Override
	public List<T> getAllAvailableEntities(LocalDateTime from, LocalDateTime until) {
		return (from == null || until == null) ? Collections.emptyList() : 
			list.values().stream().filter((Entity e) -> {
				boolean isValid = true;
				for (PeriodTuple pt : e.bookedOn) {
					if ((from.isBefore(pt.until) && from.isAfter(pt.from)) ||
					(until.isBefore(pt.until) && until.isAfter(pt.from)) ||
					(until.isEqual(pt.until) && from.isEqual(pt.from))) {
						isValid = false;
						break;
					}
				}
				return isValid;
		}).map(e -> e.entity).collect(Collectors.toList());
	}

	@Override
	public boolean isBooked(String identifier, LocalDateTime from,LocalDateTime until) {
		if (from == null || until == null || identifier ==  null) {
			return false;
		}
		List<PeriodTuple> periods = list.get(identifier).bookedOn;
		for (PeriodTuple pt : periods) {
			if ((from.isBefore(pt.until) && from.isAfter(pt.from)) ||
					(until.isBefore(pt.until) && until.isAfter(pt.from)) ||
					(until.isEqual(pt.until) && from.isEqual(pt.from))) {
				return  true;
			}
		}
		return false;
	}

	@Override
	public boolean book(Predicate<T> predicate, LocalDateTime from, LocalDateTime until) {
		if (from == null || until == null || predicate == null) {
			return false;
		}
		list.forEach((String id ,Entity e) -> {
			 if (predicate.test(e.entity) && !isBooked(id, from, until)) {
				 e.bookedOn.add(new PeriodTuple(from, until));
				 log.log("Success!");
				 return;
			 }
		});
		return true;
	}

	@Override
	public boolean book(String identifier, LocalDateTime from,LocalDateTime until) {
		if (from == null || until == null || identifier == null) {
			return false;
		}
		Entity e = list.get(identifier);
		if (e != null && !isBooked(identifier, from, until)) {
			e.bookedOn.add(new PeriodTuple(from, until));
			log.log("Success!");
			return true;
		 }
		return false;
	}
	
	

	
}
