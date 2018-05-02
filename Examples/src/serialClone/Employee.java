package serialClone;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee extends SerialCloneable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private BigDecimal salary;
	private final LocalDate hireDate;
	int mutabilityTest = 0;
	
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
	public Employee clone() throws CloneNotSupportedException {
		return (Employee) super.clone();
	}

	@Override
	public String toString() {
		return mutabilityTest + " Employee [name=" + name + ", salary=" + salary + ", hireDate=" + hireDate + "]";
	}
	
}
