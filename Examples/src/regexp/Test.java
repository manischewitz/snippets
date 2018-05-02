package regexp;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Input primer:
 * Pattern: (([1-9]|1[0-2]):([0-5][0-9]))[ap]m
 * String: 11:59am
 **/
public class Test {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in)) {
			System.out.println("Enter pattern:");
			String patternString = in.nextLine();
			
			Pattern pattern = Pattern.compile(patternString);
			while (true) {
				System.out.println("Enter string to match: ");
				String inpuString = in.nextLine();
				if (inpuString == null || inpuString.isEmpty()) {
					return;
				}
				Matcher matcher = pattern.matcher(inpuString);
				if (matcher.matches()) {
					System.out.println("Matches: ");
					int count = matcher.groupCount();
					if (count > 0) {
						for (int i = 0; i < inpuString.length(); i++) {
							//Print any empty groups
							for (int j = 1; j <= count; j++) {
								if (i == matcher.start(j) && i == matcher.end(j)) {
									System.out.print("()");
								}
							}
							//Print ( for non-empty groups starting here
							for (int j = 1; j <= count; j++) {
								if (i == matcher.start(j) && i != matcher.end(j)) {
									System.out.print("(");
								}
							}
							System.out.print(inpuString.charAt(i));
							// Print ) for non-empty groups ending here
							for (int j = 1; j <= count; j++) {
								if (i + 1 != matcher.start(j) && i + 1 == matcher.end(j)) {
									System.out.print(")");
								}
							}
						}
						System.out.print("\n");
					} else {
						System.out.print("No match.");
					}
				}
			}
		}
	}
}
