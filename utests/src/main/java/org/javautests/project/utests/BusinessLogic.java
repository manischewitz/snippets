package org.javautests.project.utests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.transform.stream.StreamSource;

public class BusinessLogic {

	public static String reverse(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		 return new StringBuilder(s).reverse().toString();
	}
	
	public static List<Integer> fetchAllNumbersWithGivenDigitsCountFromString(String seq, 
			int digitsNumber) {
		if (seq == null || digitsNumber <= 0) {
			throw new IllegalArgumentException();
		}
		List<Integer> digits = new ArrayList<>();
		StringBuilder currentSeq = new StringBuilder();
		for (char ch : seq.toCharArray()) {
			if (Character.isDigit(ch)) {
				currentSeq.append(ch);
			} else {
				if (currentSeq.length() >= digitsNumber) {
					digits.add(Integer.valueOf(currentSeq.toString()));
				}
				currentSeq.delete(0, currentSeq.length());
			}
		}
		if (currentSeq.length() >= digitsNumber) {
			digits.add(Integer.valueOf(currentSeq.toString()));
		}
		
		return digits;
	}
	
	
}
