package fundamentals;

import static java.lang.System.out;
import java.util.Scanner;

public enum EnumPrimer {

	REGULAR_EMPLOYEE("REG"),
	MANAGER("M"),
	BOSS("B"),
	EXECUTIVE("EX");
	
	private String shortAbbreviation;
	
	private EnumPrimer(String shortAbbreviation) {
		this.shortAbbreviation = shortAbbreviation;
	}

	public String getShortAbbreviation() {
		return shortAbbreviation;
	}

	public static void main(String...strings) {
		final Scanner scanner = new Scanner(System.in);
		out.println("Enter enum (REGULAR_EMPLOYEE, MANAGER, BOSS, EXECUTIVE)");
		String input = scanner.next().toUpperCase();
		EnumPrimer enumP = Enum.valueOf(EnumPrimer.class, input);
		out.println("Enum is "+enumP+" abbreviation is "+enumP.getShortAbbreviation()+
		" ordinal position is "+enumP.ordinal());
		if (enumP == EnumPrimer.BOSS) {
			out.println("Hallo boss");
		}
	
	}
}
