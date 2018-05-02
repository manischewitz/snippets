package io;

import java.io.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RandomAccessTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<Employee> stuff = new ArrayList<>();
		stuff.add(new Employee("John Doe", new BigDecimal(10501.81), 1988, 12, 15));
		stuff.add(new Employee("Anna Lee", new BigDecimal(16500.0), 1990, 12, 1));
		stuff.add(new Employee("Tony Bar", new BigDecimal(9000.64), 1978, 4, 13));
		
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream("employee.em"))) {
			for (Employee e : stuff) {
				writeData(e, out, (DataOutput dataOutput, Employee emp) -> {
					try {
						writeFixedString(emp.getName(), Employee.MAX_NAME_LENGTH, out);
						String salary = String.valueOf(emp.getSalary().doubleValue());
						writeFixedString(salary, Employee.MAX_SALARY_LENGTH, out);
						LocalDate hireDay = emp.getHireDate();
						out.writeInt(hireDay.getYear());
						out.writeInt(hireDay.getMonthValue());
						out.writeInt(hireDay.getDayOfMonth());
					} catch (IOException e1) {
						e1.printStackTrace();
						throw new RuntimeException(e1);
					}
				});
			}
		}
		
		try (RandomAccessFile raf = new RandomAccessFile("employee.em", "r")) {
			int n = (int) (raf.length() / Employee.RECORD_SIZE);
			Employee[] list = new Employee[n];
			//read in reverse order
			for (int i = n - 1; i >= 0; i--) {
				raf.seek(i * Employee.RECORD_SIZE);
				list[n - i - 1] = readData(raf, (DataInput di) -> {
					try {
						String name = readFixedString(Employee.MAX_NAME_LENGTH, raf);
						String stringSalary = readFixedString(Employee.MAX_SALARY_LENGTH, raf);
						BigDecimal salary = new BigDecimal(stringSalary);
						int year = raf.readInt();
						int month = raf.readInt();
						int day = raf.readInt();
						return new Employee(name, salary, year, month, day);
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				});
			}
			
			for (Employee em : list) {
				System.out.println(em);
			}
		}
		

	}
	
	public static <T> void writeData(T value, DataOutput dataOutput, BiConsumer<DataOutput, T> fun) {
		fun.accept(dataOutput, value);
	}
	
	public static <T> T readData(DataInput dataOutput, Function<DataInput, T> fun) {
		return fun.apply(dataOutput);
	}
	
	public static void writeFixedString(String s, int size, DataOutput out) throws IOException {
		for (int i = 0; i < size; i++) {
			char ch = 0;
			if (i < s.length()) {
				ch = s.charAt(i);
			}
			out.writeChar(ch);
		}
	}
 
	public static String readFixedString(int size, DataInput in) throws IOException {
		StringBuilder sb = new StringBuilder(size);
		int i = 0;
		boolean more = true;
		while (more && i < size) {
			char ch = in.readChar();
			i++;
			if (ch == 0) {
				more = false;
			} else {
				sb.append(ch);
			}
		}
		in.skipBytes(2 * (size - i));
		return sb.toString();
	}
}
