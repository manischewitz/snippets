package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PostTest {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in, "UTF-8")) {
			while (true) {
				System.out.println("Enter path to property file:");
				String path = in.nextLine();
				if (path == null || path.isEmpty()) {
					System.out.println("BYE.");
					break;
				}
				Properties properties = new Properties();
				try (InputStream is = Files.newInputStream(Paths.get(path))) {
					properties.load(is);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					break;
				}
				String urlString = properties.remove("url").toString();
				Object userAgent = properties.remove("User-Agent");
				Object redirects = properties.remove("redirects");
				CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
				String result;
				try {
					result = doPost(new URL(urlString), properties,
					userAgent == null ? null : userAgent.toString(),
					redirects == null ? -1 : Integer.parseInt(redirects.toString()));
					System.out.println(result);
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
		}

	}

	private static String doPost(URL url, Properties properties, String userAgent, int redirects) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (userAgent != null) {
			connection.setRequestProperty("User-Agent", userAgent);
		}
		if (redirects >= 0) {
			connection.setInstanceFollowRedirects(false);
		}
		connection.setDoOutput(true);
		try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
			boolean first = true;
			for (Map.Entry<Object, Object> pair : properties.entrySet()) {
				if (first) {
					first = false;
				} else {
					out.print('&');
				}
					String name = pair.getKey().toString();
					String value = pair.getValue().toString();
					out.print(name + "=" + URLEncoder.encode(value, "UTF-8"));
			}
			//When switching from writing to reading any part of the response, 
			//the actual interaction with the server happens.
			String encoding = connection.getContentEncoding();
			if (encoding == null) {
				encoding = "UTF-8";
			}
			
			if (redirects > 0) {
				int responseCode = connection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_MOVED_PERM
						|| responseCode == HttpURLConnection.HTTP_MOVED_TEMP
						|| responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
					String location = connection.getHeaderField("Location");
					if (location != null) {
						URL base = connection.getURL();
						connection.disconnect();
						return doPost(new URL(base, location), properties, 
								userAgent, redirects - 1);
					}
				}
			} else if (redirects == 0) {
				throw new IOException("Too many redirects.");
			}
			StringBuilder responce = new StringBuilder();
			try (Scanner in = new Scanner(connection.getInputStream(), encoding)) {
				while (in.hasNextLine()) {
					responce.append(in.nextLine());
					responce.append('\n');
				}
			} catch (IOException e) {
				InputStream err = connection.getErrorStream();
				if (err == null) {
					throw e;
				}
				try (Scanner in = new Scanner(err, encoding)) {
					responce.append(in.nextLine());
					responce.append('\n');
				}
			}
			return responce.toString();
		} 
	}

}
