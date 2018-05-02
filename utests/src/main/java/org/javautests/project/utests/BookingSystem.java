package org.javautests.project.utests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingSystem {

	private final String resourse;
	private final List<LocalDateTime> booked;
	private final LocalDate now;
	
	public BookingSystem(String string, LocalDate localDate) {
		this.resourse = string;
		booked = new ArrayList<>();
		this.now = localDate;
	}

	public List<LocalDateTime> getBookedHoursList() {
		return Collections.unmodifiableList(booked);
	}

	public boolean book(LocalDateTime of) {
		if(of != null && !isBookedOn(of) && of.toLocalDate().equals(now)) {
			return booked.add(of);
		}
		return false;
	}

	public String getResourse() {
		return resourse;
	}

	public boolean isBookedOn(LocalDateTime of) {
		for (LocalDateTime ldt : booked) {
			if (of.getHour() == ldt.getHour()) {
				return true;
			}
		}
		return false;
	}

	

	
	
}
