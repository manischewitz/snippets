package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObjectStreamTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		Employee john = new Employee("John Doe", new BigDecimal(10501.81), 1988, 12, 15);
		Manager anna = new Manager("Anna Lee", new BigDecimal(16500.0), 1990, 12, 1);
		anna.setSecretary(john);
		Manager tony = new Manager("Tony Bar", new BigDecimal(9000.64), 1978, 4, 13);
		tony.setSecretary(john);
		
		List<Employee> list = new ArrayList<>();
		list.add(john);
		list.add(anna);
		list.add(tony);
		
		try (ObjectOutputStream out =
				new ObjectOutputStream(new FileOutputStream("employee.data"))) {
			out.writeObject(list);
		}
		
		try (ObjectInputStream in = 
				new ObjectInputStream(new FileInputStream("employee.data"))) {
			list = (List<Employee>) in.readObject();
			
			//check if two managers share the same secretary
			list.get(0).mutabilityTest = 777;
			
			list.forEach(System.out::println);
		}

	}

}
