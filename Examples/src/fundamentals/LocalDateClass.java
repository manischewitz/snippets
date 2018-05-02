package fundamentals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.lang.System.out;

public class LocalDateClass {

	//Unlike Date class LocalDate has no state
	public static void main(String[] args) {
		LocalDate date = LocalDate.now();
		int month = date.getMonthValue();
		int today = date.getDayOfMonth();
		
		date = date.minusDays(today - 1); //set to start of month
		DayOfWeek weekday = date.getDayOfWeek();
		int value = weekday.getValue();
		
		out.println("Mon Tue Wed Thu Fri Sat Sun");
		for (int i = 1; i < value; i++) {
			out.print("    ");
		}
		while (date.getMonthValue() == month) {
			out.printf("%3d", date.getDayOfMonth());
			if (date.getDayOfMonth() == today) {
				out.print("*");
			} else {
				out.print(" ");
			}
			date = date.plusDays(1);
			if (date.getDayOfWeek().getValue() == 1) {
				out.print('\n');
			}
		}
		if (date.getDayOfWeek().getValue() != 1) {
			out.print('\n');
		}
	}

}
