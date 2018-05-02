package org.javautests.project.booking;

import static org.junit.Assert.*;

import org.javautests.project.race.Logging;
import org.javautests.project.race.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.mockito.Mockito.*
;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

@RunWith(JUnitParamsRunner.class)
public class BookingSystemTest {

	private BookingSystem<Classroom> bookingSystem;
	private Classroom classroom;
	private static final LocalDateTime VALID_TIME = LocalDateTime.of(3000, 7, 14, 7, 55);
	private Logging log;
	
	@Before
	public void setUp() throws Exception {
		classroom = mock(Classroom.class);
		log = mock(Logging.class);
		when(classroom.isHasProjector()).thenReturn(true);
		when(classroom.getCapacity()).thenReturn(100);
		bookingSystem = new BookingSystemImpl<>(log);
	}
	
	private final static Object[] getLegalParams() {
		return new Object[] {
			new Object[] {VALID_TIME, VALID_TIME.plusMinutes(5)},
			new Object[] {VALID_TIME, VALID_TIME.plusHours(1)},
		};
	}
	
	private final static Object[] getUnacceptableParams() {
		return new Object[] {
			new Object[] {null, null, null, null},
		};
	}
	
	private final static Object[] getAcceptableParamsWithPredicate() {
		Classroom c = new Classroom();
		c.setProjector(true);
		c.setCapacity(100);
		return new Object[] {
				new Object[] {(Predicate<Classroom>) 
						(Classroom cl) -> { return cl.isHasProjector() && cl.getCapacity() > 10; },
						VALID_TIME, VALID_TIME.plusHours(1), c}
		};
	}

	@Test
	public void shouldAddEntity() {
		assertEquals(0, bookingSystem.getEntities().size());
		bookingSystem.addEntity(classroom, "class1");
		assertEquals(1, bookingSystem.getEntities().size());
	}
	
	@Test
	@Parameters(method = "getLegalParams")
	public void enumerateAllAvailableEntitiesForGivenHourAndDay(LocalDateTime from, 
			LocalDateTime until) {
		assertEquals(0, bookingSystem.getEntities().size());
		bookingSystem.addEntity(classroom, "class 1");
		bookingSystem.addEntity(mock(Classroom.class), "calss2");
		bookingSystem.book("class 1", from, until);
		assertEquals(1, bookingSystem.getAllAvailableEntities(from, until).size()); 
		assertNotSame(classroom, bookingSystem.getAllAvailableEntities(from, until).get(0));
	}
	
	@Test
	@Parameters(method = "getLegalParams")
	public void shouldBookSpecificEntityByName(LocalDateTime from, LocalDateTime until) {
		assertEquals(0, bookingSystem.getEntities().size());
		bookingSystem.addEntity(classroom, "class 1");
		
		assertFalse(bookingSystem.isBooked("class 1", from, until));
		bookingSystem.book("class 1", from, until);
		verify(log, times(1)).log(any());
		assertTrue(bookingSystem.isBooked("class 1", from, until));
	}
	
	@Test
	@Parameters(method = "getUnacceptableParams")
	public void shouldNotAccept(String name, LocalDateTime from, 
			LocalDateTime until, Classroom classroom) {
		assertNull(bookingSystem.addEntity(classroom, name));
		assertFalse(bookingSystem.isBooked(name,  from, until));
		assertFalse(bookingSystem.book(name,  from, until));
		assertEquals(0, bookingSystem.getAllAvailableEntities( from, until).size());
	}
	
	@Test
	public void shouldReturnOldValue() {
		String key = "class";
		bookingSystem.addEntity(classroom, key);
		assertSame(classroom, bookingSystem.addEntity(mock(Classroom.class), key));
	}
	
	@Test
	@Parameters(method = "getAcceptableParamsWithPredicate")
	public void shouldBookSpecificEntityByConstraints(Predicate<Classroom> predicate, 
			LocalDateTime from, LocalDateTime until, Classroom c) {
		assertEquals(0, bookingSystem.getEntities().size());
		bookingSystem.addEntity(c, "class 1");
		
		assertFalse(bookingSystem.isBooked("class 1",  from, until));
		
		bookingSystem.book(predicate,  from, until);
		verify(log, times(1)).log(any());
		assertTrue(bookingSystem.isBooked("class 1",  from, until));
	}
	
	@Test
	public void shouldListAllExistingEntities() {
		assertEquals(0, bookingSystem.getEntities().size());
		
		bookingSystem.addEntity(classroom, "class 1");
		bookingSystem.addEntity(mock(Classroom.class), "class 2");
		
		assertEquals(2, bookingSystem.getEntities().size());
	}

}
