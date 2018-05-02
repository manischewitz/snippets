package signing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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

public class RSATest {

	private static enum Operation {
		GENKEY, ENCRYPT, DECRYPT;
	};
	
	public static void main(String[] args) throws NoSuchAlgorithmException, ClassNotFoundException, 
	NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		try (Scanner input = new Scanner(System.in, "UTF-8")) {
			System.out.println("Enter key size (256, 512 ...):");
			int key = 0;
			try {
				 key = input.nextInt();
				 input.nextLine();
			} catch (Exception e) {
				System.out.println("Try again.");
				main(args);
			}
			System.out.println("genkey privateKeyFileName publicKeyFileName");
			System.out.println("encrypt sourceFile encryptedFile publicKeyFile");
			System.out.println("decrypt encryptedFile decryptedFile privateKeyFile");
			String[] in;
			Operation command;
			while (true) {
				try {
					in = input.nextLine().trim().split("\\s+");
					if (in.length > 0 && in[0].equals("exit")) {
						break;
					} else if (in.length > 0) {
						command = Enum.valueOf(Operation.class, in[0].trim().toUpperCase());
					} else {
						continue;
					} 
					
					switch (command) {
					case DECRYPT: {
						if (in.length >= 4) {
							decrypt(in[3], in[1], in[2]);
						}
					} break;
					case ENCRYPT: {
						if (in.length >= 4) {
							encrypt(in[3], in[2], in[1]);
						}
					} break;
					case GENKEY: {
						if (in.length >= 3) {
							generateKeyPair(key, in[1], in[2]);
						}
					} break;
					}
					
				} catch (IllegalArgumentException iae) {
					System.out.println("Command is not found.");
				} catch (IOException e) {
					System.out.println("File is not found.");
				} catch (InvalidKeyException e) {
					System.out.println("Key usage excepton.");
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void generateKeyPair(int keysize, String privateKeyFileName, String publicKeyFileName) 
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = new SecureRandom();
		keyPairGenerator.initialize(keysize, random);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		try (ObjectOutputStream out = new ObjectOutputStream(new
				FileOutputStream(privateKeyFileName))) {
			out.writeObject(keyPair.getPrivate());
		}
		try (ObjectOutputStream out = new ObjectOutputStream(new
				FileOutputStream(publicKeyFileName))) {
			out.writeObject(keyPair.getPublic());
		}
	}
	
	private static void encrypt (String publicKeyFile, String encryptedFile, String sourceFile) throws 
	IOException, NoSuchAlgorithmException, InvalidKeyException,
	NoSuchPaddingException, ClassNotFoundException, IllegalBlockSizeException, 
	ShortBufferException, BadPaddingException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom random = new SecureRandom();
		keygen.init(random);
		SecretKey key = keygen.generateKey();
		// wrap with RSA public key
		try (ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(publicKeyFile));
				DataOutputStream out = new DataOutputStream(new FileOutputStream(encryptedFile));
				InputStream in = new FileInputStream(sourceFile)) {
			Key publicKey = (Key) keyIn.readObject();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.WRAP_MODE, publicKey);
			byte[] wrappedKey = cipher.wrap(key);
			out.writeInt(wrappedKey.length);
			out.write(wrappedKey);
			
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			Util.crypt(in, out, cipher);
		}
	}
	
	private static void decrypt(String privateKeyFile, String encryptedFile, String decryptedFile) throws 
	IOException, ClassNotFoundException, NoSuchAlgorithmException, 
	NoSuchPaddingException, InvalidKeyException, ShortBufferException, 
	IllegalBlockSizeException, BadPaddingException {
		try (DataInputStream in = new DataInputStream(new FileInputStream(encryptedFile));
				ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(privateKeyFile));
					OutputStream out = new FileOutputStream(decryptedFile)) {
			int length = in.readInt();
			byte[] wrappedKey = new byte[length];
			in.read(wrappedKey, 0, length);
			
			// unwrap with RSA private key
			Key privateKey = (Key) keyIn.readObject();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.UNWRAP_MODE, privateKey);
			Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			Util.crypt(in, out, cipher);
		}
	} 
	
	
	
	
	
	
	
	
	

}
