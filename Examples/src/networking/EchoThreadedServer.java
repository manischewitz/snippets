package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class EchoThreadedServer {

	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(8080)) {
			int i = 1;
			while (true) {
				Socket incoming = ss.accept();
				System.out.println("Spawning " + i);
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						try (InputStream is = incoming.getInputStream();
								OutputStream os = incoming.getOutputStream();
								Scanner in = new Scanner(is, "UTF-8");) {
							PrintWriter out = new PrintWriter(
									new OutputStreamWriter(os, "UTF-8"), true);
							out.println("SHUTDOWN to exit.");
							boolean done = false;
							String line;
							while (!done && in.hasNextLine()) {
								line = in.nextLine();
								System.out.println("Incoming: "+line);
								out.println("Echo: " + line);
								if (line.trim().equals("SHUTDOWN")) {
									done = true;
								}
							}
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
						
					}
					
				};
				Thread thread = new Thread(runnable);
				thread.start();
				i++;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

}
