package networking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class URLCoonnectionTest {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in, "UTF-8")) {
			while (true) {
				System.out.println("Enter character sequence in format: "
						+ "https://www.oracle.com/ user password -verbose(optional)");
				String urlName = in.nextLine();
				if (urlName == null || urlName.isEmpty()) {
					System.out.println("BYE.");
					break;
				}
				try {
					List<String> tokens = Arrays.asList(urlName.split(" "));
					URL url = new URL(tokens.get(0));
					URLConnection connection = url.openConnection();
					if (tokens.size() > 2) {
						String credentials = tokens.get(1) + ":" + tokens.get(2);
						Base64.Encoder encoder = Base64.getEncoder();
						String encoding = encoder.encodeToString(
								credentials.getBytes(StandardCharsets.UTF_8));
						connection.setRequestProperty("Authorization", "Basic " + encoding);
					}
					connection.connect();
					final Map<String, List<String>> headers = 
							connection.getHeaderFields();
					headers.forEach((String k, List<String> v) -> {
						v.forEach((String val) -> {
							System.out.println(k + ": " + val);
						});
					});
					System.out.println("---------- ");
					System.out.println("getContentType: " + connection.getContentType());
					System.out.println("getContentLength: " + connection.getContentLength());
					System.out.println("getContentEncoding: " + connection.getContentEncoding());
					System.out.println("getDate: " + connection.getDate());
					System.out.println("getExpiration: " + connection.getExpiration());
					System.out.println("getLastModifed: " + connection.getLastModified());
					System.out.println("---------- ");
					
					String encoding = connection.getContentEncoding();
					if (encoding == null) {
						encoding = "UTF-8";
					} 
					
					if (tokens.contains("-verbose")) {
						try (Scanner input = new Scanner(connection.getInputStream())) {
							for (int line = 1; input.hasNextLine(); line++) {
								System.out.println(line +": "+ input.nextLine());
							}
						}
					}
				} catch (IOException e) {
					System.out.println("No protocol is specified, "
							+ "or an unknown protocol is found.");
					continue;
				}
			}
		}

	}

}
