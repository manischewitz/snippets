package fundamentals;

import java.util.Arrays;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class LambdaPrimer {

	public static void main(String[] args) {
		String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars", 
	            "Jupiter", "Saturn", "Uranus", "Neptune" };
	      System.out.println(Arrays.toString(planets));
	      System.out.println("Sorted in dictionary order:");
	      Arrays.sort(planets);
	      System.out.println(Arrays.toString(planets));
	      System.out.println("Sorted by length:");
	      Arrays.sort(planets, (first, second) -> first.length() - second.length());
	      System.out.println(Arrays.toString(planets));
	      Arrays.sort(planets, String::compareToIgnoreCase);
	      System.out.println(Arrays.toString(planets));
	      //is the same as
	      Arrays.sort(planets, (a, b) -> a.compareToIgnoreCase(b));
	      System.out.println(Arrays.toString(planets));
	            
	      Timer t = new Timer(1000, event ->
	         System.out.println("The time is " + new Date()));
	      Timer timer1 = new Timer(1000, event -> System.out.println(event));
	      // is exactly the same as
	      Timer timer2 = new Timer(1000, System.out::println);
	      t.start();   
	         
	      /*
	      JOptionPane.showMessageDialog(null, "Quit program?");
	      System.exit(0);    
	       */
	      
	}

}
