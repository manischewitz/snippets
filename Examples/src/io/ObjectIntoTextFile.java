package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectIntoTextFile {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		List<Employee> stuff = new ArrayList<>();
		stuff.add(new Employee("John Doe", new BigDecimal(10501.81), 1988, 12, 15));
		stuff.add(new Employee("Anna Lee", new BigDecimal(16500.0), 1990, 12, 1));
		stuff.add(new Employee("Tony Bar", new BigDecimal(9000.64), 1978, 4, 13));
		
		try (PrintWriter out = new PrintWriter("Employee.dat", "UTF-8")) {
			writeData(stuff, out, (Employee e) -> {
				StringBuffer sb = new StringBuffer();
				sb.append(e.getName());
				sb.append("|");
				sb.append(e.getSalary().doubleValue());
				sb.append("|");
				sb.append(e.getHireDate());
				return sb.toString();
			});
		}
		
		try (Scanner in = new Scanner(new FileInputStream("Employee.dat"), "UTF-8")) {
			List<Employee> list = readData(in, (Scanner s) -> {
				String line = in.nextLine();
				String[] tokens = line.split("\\|");
				String name = tokens[0];
				BigDecimal salary = new BigDecimal(tokens[1]);
				LocalDate hd = LocalDate.parse(tokens[2]);
				return new Employee(name, salary, hd.getYear(), hd.getMonthValue(), hd.getDayOfMonth());
			}, ArrayList::new);
			list.forEach((Employee e) -> {System.out.println(e);});
		}

	}

	private static <T, U> void writeData(Collection<T> objects, PrintWriter out, Function<T, U> fun) {
		out.println(objects.size());
		for (T e : objects) {
			out.println(fun.apply(e));
		}
	}
	
	private static <T, U extends Collection<T>> U readData(Scanner in, Function<Scanner, T> fun, Supplier<U> sup) {
		int n = in.nextInt();
		in.nextLine();
		
		U collection = sup.get();
		for (int i = 0; i < n; i++) {
			collection.add(fun.apply(in));
		}
		return collection;
	}
	
}
