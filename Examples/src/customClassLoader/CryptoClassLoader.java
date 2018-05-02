package customClassLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CryptoClassLoader extends ClassLoader {

	private int key;
	
	public CryptoClassLoader (int key) {
		this.key = key;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			byte[] classBytes = null;
			classBytes = loadClassBytes(name);
			Class<?> c = super.defineClass(name, classBytes, 0, classBytes.length);
			if (c == null) {
				throw new ClassNotFoundException(name);
			}
			return c;
		} catch (IOException ioe) {
			throw new ClassNotFoundException(name);
		}
	}

	private byte[] loadClassBytes(String name) throws IOException {
		String className = name.replace('.', '/') + ".caesar";
		byte[] bytes = Files.readAllBytes(Paths.get(className));
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (bytes[i] - key); 
		}
		return bytes;
	}
}
