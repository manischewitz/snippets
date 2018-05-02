package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoSingleUserServer {

	public static void main(String...strings) throws IOException {
		//establish server socket
		try (ServerSocket ss = new ServerSocket(7777)) {
			//wait for client connect
			try (Socket incoming = ss.accept()) {
				InputStream is = incoming.getInputStream();
				OutputStream os = incoming.getOutputStream();
				
				try (Scanner in = new Scanner(is, "UTF-8")) {
					PrintWriter out = new PrintWriter(
							new OutputStreamWriter(os, "UTF-8"), true);
					out.println("SHUTDOWN to exit.");	
					
					boolean done = false;
					String line;
					while (!done && in.hasNextLine()) {
						line = in.nextLine();
						out.println("Echo: " + line);
						if (line.trim().equals("SHUTDOWN")) {
							done = true;
						}
					}
				}
			}
			
		}
	}
	
}
