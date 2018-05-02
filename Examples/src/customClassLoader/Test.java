package customClassLoader;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.logging.Logger;


public class Test {

	public static void main(String[] args) throws Exception {
		try (Scanner in = new Scanner(System.in)) {
			while (true) {
					Logger.getGlobal().info("Expected input:\n1. PathToClassToLoad key \n2. exit");
					Logger.getGlobal().info("PathToClassToLoad is a dot separated path to class with main method.");
					
					final String input = in.nextLine();
					if (input.equals("exit")) {
						break;
					} else {
						String[] params = input.trim().split("\\s+");
						runClass(params[0], params[1]);
					}
			}
		} 

	}
	
	private static void runClass(String name, String key) throws Exception {
		try {
			ClassLoader loader = new CryptoClassLoader(Integer.parseInt(key));
			Class<?> c = loader.loadClass(name);
			Method method = c.getMethod("main", String[].class);
			method.invoke(null, (Object) new String[] {});
		} catch (Throwable e) {
			Exception ex = new RuntimeException();
			ex.initCause(e);
			throw ex;
		}
	}

}
