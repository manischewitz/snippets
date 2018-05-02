package dateAndTime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class LocalDates {

	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		System.out.println("Today " + today);
		
		LocalDate myBirthDay = LocalDate.of(2010, Month.MARCH, 8);
		myBirthDay = LocalDate.of(2010, 3, 8);
		System.out.println("My Birth Day " + myBirthDay);
		
		LocalDate programmersDay = LocalDate.of(2018, 1, 1)
				.plusDays(255);
		System.out.println("programmersDay: " + programmersDay);
		
		LocalDate independenceDay = LocalDate.of(2018, Month.JULY, 4);
		LocalDate christmas = LocalDate.of(2018, Month.DECEMBER, 25);
		
		System.out.println("Until christmas: " + 
		independenceDay.until(christmas, ChronoUnit.DAYS));
		
		System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
		System.out.println(LocalDate.of(2016, 3, 31).minusMonths(1));
		
		DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
		System.out.println("startOfLastMillennium: " + startOfLastMillennium);
		System.out.println(startOfLastMillennium.getValue());
		System.out.println(DayOfWeek.SATURDAY.plus(3));
	}

}
