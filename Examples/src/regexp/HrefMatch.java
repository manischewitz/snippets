package regexp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Example: http://java.sun.com
public class HrefMatch {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in)) {
			while (true) {
				System.out.println("Enter URL:");
				String url = in.nextLine();
				if (url.isEmpty()) {
					break;
				}
				match(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void match(String url) 
			throws MalformedURLException, IOException {
		InputStreamReader in = new InputStreamReader(
				new URL(url).openStream(), StandardCharsets.UTF_8);
		StringBuilder sb = new StringBuilder();
		int ch;
		while ((ch = in.read()) != -1) {
			sb.append((char) ch);
		}
		
		String stringPattern = "<a\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*>";
		Pattern pattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sb);
		
		while (matcher.find()) {
			String match = matcher.group();
			System.out.println(match);
		}
	}

}
