package main;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	
	
	
	public static void main(String[] args) {
		//SpringApplication.run(Main.class, args);
		List<String> safeStrings =  Collections.checkedList(new ArrayList<String>(), String.class);
		List rawList = safeStrings;
		rawList.add(new Date()); 
		
		Date[] date = new Date[10];
		for (int i = 0; i < 10; i++) {
			date[i] = new Date();
		} 
		//Arrays.asList(date).remove(0);
		
	}
}
