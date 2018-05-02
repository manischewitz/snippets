package fundamentals;

import java.math.BigDecimal;
import java.util.Date;

public class ObjectClonning implements Cloneable {

	private String name;
	private BigDecimal salary;
	private Date hireDate; //mutable (!)
	
	public ObjectClonning(String name, BigDecimal salary, Date hireDate) {
		this.name = name;
		this.salary = salary;
		this.hireDate = hireDate;
	}
	
	public ObjectClonning() { }

	@Override
	public ObjectClonning clone() throws CloneNotSupportedException {
		ObjectClonning cloned = (ObjectClonning) super.clone();
		cloned.hireDate = getHireDate();
		return cloned;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public Date getHireDate() {
		return (Date) hireDate.clone();
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", salary=" + salary + ", hireDate=" + hireDate + "]";
	}
	
	public static void main(String...strings) {
		try {
			ObjectClonning original = new ObjectClonning("Anna Lee", new BigDecimal(770.25), new Date());
			ObjectClonning copied = original.clone();
			original.name = "John Doe";
			
			System.out.println(original);
			System.out.println(copied);
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
	}
	
}
