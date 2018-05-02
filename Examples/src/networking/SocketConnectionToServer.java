package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Scanner;

import sun.awt.CharsetString;

/**
 * This program makes a socket connection to the atomic clock in Boulder, 
 * Colorado, and prints the time that the server sends.
 * */
public class SocketConnectionToServer {
/*
	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socket = new Socket("time-a.nist.gov", 13);
				Scanner scanner = new Scanner(socket.getInputStream(), "UTF-8")) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
			}
		}

	}
*/
	public static void main(String...strings) {
		try (Socket s = new Socket("localhost", 8080);
				Scanner fromServer = new Scanner(s.getInputStream());
					OutputStream toServer = s.getOutputStream();
					 Scanner in = new Scanner(System.in, "UTF-8")) {
				while (true) {
					if (fromServer.hasNextLine()) {
						System.out.println(fromServer.nextLine());
					}
					toServer.write(in.nextLine().getBytes());
					s.shutdownOutput();
				}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
