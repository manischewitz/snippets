package networking;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import java.util.Scanner;

public class InternetAdresses {

	public static void main(String[] args) throws UnknownHostException {
		try (Scanner in = new Scanner(System.in, StandardCharsets.UTF_8.toString())) {
			dialog(in, "empty");
		}

	}
	
	private static void dialog(Scanner in, String hostname) {
		System.out.println("Enter hostname or 'exit' or 'local':");
		hostname = in.nextLine();
		if (hostname == null || hostname.equals("exit")) {
			System.out.println("Bye.");
			return;
		}
		try {
			if (hostname.equals("local")) {
				InetAddress local = InetAddress.getLocalHost();
				System.out.println(local.toString());
			} else {
				InetAddress[] addresses = InetAddress.getAllByName(hostname);
				for (InetAddress a : addresses) {
					System.out.println(a.toString());
				}
			}
 		} catch (UnknownHostException uhe) {
			System.out.println("No IP address for the host could be found.");
		}
		dialog(in, hostname);
	}

}
