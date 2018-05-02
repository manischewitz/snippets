package signing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Scanner;

public class Digest {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in, "UTF-8")) {
			while (true) {
				try {
					System.out.println("Enter algorithm name such as SHA-1, SHA-256, or MD5:");
					String input = in.nextLine();
					if (input != null && !input.isEmpty()) {
						if (input.equals("exit")) {
							break;
						}
						MessageDigest md = MessageDigest.getInstance(input);
						System.out.println("Enter path to file:");
						input = in.nextLine();
						byte[] inputBytes = Files.readAllBytes(Paths.get(input));
						byte[] hash = md.digest(inputBytes);
						String digest = "";
						for (int i = 0; i < hash.length; i++) {
							int v = hash[i] & 0xFF;
							if (v < 16) {
								digest += '0';
							}
							digest += Integer.toString(v, 16).toUpperCase() + " ";
						}
						System.out.println(digest);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
