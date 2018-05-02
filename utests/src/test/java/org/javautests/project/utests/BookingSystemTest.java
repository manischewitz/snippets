package org.javautests.project.utests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

public class BookingSystemTest {

	private BookingSystem bs = new BookingSystem("Desk", LocalDate.of(2018, 04, 30));
	
	@Test
	public void shouldReturnListOfBookedHours() {
		assertEquals(0, bs.getBookedHoursList().size());
		bs.book(LocalDateTime.of(2018, 04, 30, 3, 00));
		bs.book(LocalDateTime.of(2018, 04, 30, 4, 00));
		bs.book(LocalDateTime.of(2018, 04, 30, 5, 00));
		assertEquals(3, bs.getBookedHoursList().size());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void returnedListOfBookedHoursShouldNotMutate() {
		List<LocalDateTime> list = bs.getBookedHoursList();
		list.add(LocalDateTime.of(2018, 04, 30, 4, 00));
	}
	
	@Test
	public void shouldBookProperly() {
		bs.book(LocalDateTime.of(2018, 04, 30, 3, 00));
		assertTrue(bs.isBookedOn(LocalDateTime.of(2018, 04, 30, 3, 00)));
	}
	
	@Test
	public void shouldContainBookedResource() {
		assertEquals("Desk" ,bs.getResourse());
	}	
	
	@Test
	public void shouldNotBookOnAlreadyBookedHour() {
		bs.book(LocalDateTime.of(2018, 04, 30, 3, 00));
		assertFalse(bs.book(LocalDateTime.of(2018, 04, 30, 3, 30)));
	}
	
	@Test()
	public void shouldNotAcceptIllegalHours() {
		assertFalse(bs.book(null));
		assertFalse(bs.isBookedOn(null));
		assertFalse(bs.book(LocalDateTime.of(1, 04, 30, 3, 00)));
	}	
	
}
