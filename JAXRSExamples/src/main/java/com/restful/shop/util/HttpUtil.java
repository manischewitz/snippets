package com.restful.shop.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import org.jboss.resteasy.util.Base64;

import com.restful.shop.domain.Customer;
import com.restful.shop.domain.LastView;
import com.restful.shop.domain.LastViewed;

public class HttpUtil {
	
	public enum Algorithm {
		MD5("MD5"),
		SHA1("SHA-1"),
		SHA256("SHA-256");
		
		String name;
		
		Algorithm(String name) {
			this.name = name;
		}
	}
	
	/**
	 * Returns string digest representation of Serializable or null if exception occurs. May return an empty string.
	 * Ensure all underlying objects also implement java.io.Serializable.
	 * */
	public static String hash(Algorithm alg, Serializable object) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			MessageDigest md = MessageDigest.getInstance(alg.name);
			oos.writeObject(object);
			byte[] hash = md.digest(baos.toByteArray());
			String digest = "";
			for (int i = 0; i < hash.length; i++) {
				int v = hash[i] & 0xFF;
				if (v < 16) {
					digest += '0';
				}
				digest += Integer.toString(v, 16).toUpperCase();
			}
			return digest;
			} catch (Exception e) {
					e.printStackTrace();
					return null;
			}
	}
	
	public static String generateOtpToken(String secret) {
	      long minutes = System.currentTimeMillis() / 1000 / 60;
	      String concat = secret + minutes;
	      MessageDigest digest = null;
	      try {
	         digest = MessageDigest.getInstance("MD5");
	      } catch (NoSuchAlgorithmException e) {
	         throw new IllegalArgumentException(e);
	      }
	      byte[] hash = digest.digest(concat.getBytes(Charset.forName("UTF-8")));
	      return Base64.encodeBytes(hash);
	   }
	
	
	public static void main(String...strings) {
		Customer customer = new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU");
		Customer target = new Customer(2, "Anna", "Lee", "Main street", "Moscow", "Russia", "64128", "RU");
		
		LastView entity = new LastView(LocalDate.now().toString(), customer);
		LastViewed<Customer> lv = new LastViewed<>(target, new ArrayList<LastView>() { { add(entity ); } });
		
		System.out.println(hash(Algorithm.SHA256, lv));
		System.out.println(hash(Algorithm.MD5, lv).equals(hash(Algorithm.MD5, lv)) );
		LastViewed<Customer> lv1 = new LastViewed<>(target, new ArrayList<LastView>());
		System.out.println(hash(Algorithm.MD5, lv).equals(hash(Algorithm.MD5, lv1)) );
		
	}
}
