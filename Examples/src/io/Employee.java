package io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final BigDecimal salary;
	private final LocalDate hireDate;
	int mutabilityTest = 0;
	public static final int MAX_NAME_LENGTH = 36;
	public static final int MAX_SALARY_LENGTH = 128;
	/**
	 * 36 chars for name = 64 bytes for name
	 * salary in string format = 256 bytes
	 * 3 int = 12 bytes for the date
	 **/
	public static final int RECORD_SIZE = (MAX_NAME_LENGTH * 2) + 256 + (3 * 4);
	
	public Employee(String name, BigDecimal salary, int year, int month, int day) {
		this.name = name;
		this.salary = salary;
		hireDate = LocalDate.of(year, month, day);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}
	
	@Override
	public String toString() {
		return "Employee [name=" + name + ", salary=" + salary + ", hireDate=" + hireDate + "]" + this.mutabilityTest;
	}
	
}
