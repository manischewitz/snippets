package signing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
//so called symmetcric cipher
public class AESTest {

	private static enum Operation {
			GENKEY, ENCRYPT, DECRYPT;
	};
	
	public static void main(String[] args) throws NoSuchAlgorithmException, 
	NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		try (Scanner in = new Scanner(System.in, "UTF-8")) {
			System.out.println("genkey fileName");
			System.out.println("encrypt plainFile encryptedFile secretKey");
			System.out.println("decrypt encryptedFile decryptedFile secretKey");
			while (true) {
				String input = in.nextLine();
				if (input == null || input.isEmpty()) {
					input = in.nextLine();
				} else if (input.equals("exit")) {
					break;
				}
				try {
					String[] params = input.trim().split("\\s+");
					Operation value;
					if (params.length >= 1) {
						value = Enum.valueOf(Operation.class, params[0].trim().toUpperCase());
						switch (value) {
						case DECRYPT: {
							if (params.length >= 4) {
								cipher(Cipher.DECRYPT_MODE, params[1], params[2], params[3]);
							}
						} break;
						case ENCRYPT: {
							if (params.length >= 4) {
								cipher(Cipher.ENCRYPT_MODE, params[1], params[2], params[3]);
							}
						} break;
						case GENKEY: {
							if (params.length >= 2) {
								generateKey(params[1]);
							}
						} break;
						}
					}
				} catch (IllegalArgumentException iae) {
					System.out.println("Command is not found.");
				} catch (IOException e) {
					System.out.println("Filesystem exception.");
				} catch (InvalidKeyException | ClassNotFoundException e) {
					System.out.println("Key exception.");
				} 
			}
		}

	}
	
	private static void cipher(int mode, String from, String to, String keyfile) 
			throws IOException, ClassNotFoundException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyfile));
				InputStream in = new FileInputStream(from);
				OutputStream out = new FileOutputStream(to)) {
			Key key = (Key) ois.readObject();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(mode, key);
			Util.crypt(in, out, cipher);
		}
	}
	
	private static void generateKey(String path) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecureRandom random = new SecureRandom();
			keygen.init(random);
			SecretKey key = keygen.generateKey();
			try (ObjectOutputStream ois = new ObjectOutputStream(
					new FileOutputStream(path))) {
				ois.writeObject(key);
			}
	}

}
