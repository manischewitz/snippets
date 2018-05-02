

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Caesar {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in)) {
			while (true) {
				Logger.getGlobal().info("Expected input:\n1. source target key \n2. exit");
				/*
				StringTokenizer tokens = new StringTokenizer("Hello I'm your String", " ");
    				String[] splited = new String[tokens.countTokens()];
    				int index = 0;
    				while(tokens.hasMoreTokens()){
        			splited[index] = tokens.nextToken();
        			++index;
    				}
				*/
				final String input = in.nextLine();
				if (input.equals("exit")) {
					break;
				} else {
					String[] params = input.trim().split("\\s+");
					if (params.length < 3) {
						continue;
					}
					try  (FileInputStream inStream = new FileInputStream(params[0]);
							FileOutputStream outStream = new FileOutputStream(params[1])) {
							int key = Integer.parseInt(params[2]);
							int ch;
							while ((ch = inStream.read()) != -1) {
								byte c = (byte) (ch + key);
								outStream.write(c);
							}
					} catch (IOException ioe) {
						Logger.getGlobal().info("Fail while loading file.");
					} catch (NumberFormatException nfe) {
						Logger.getGlobal().info("Only digits are allowed as last token.");
					}
				}
			}
		}

	}

}
