package serialClone;

import java.math.BigDecimal;



public class Test {

	public static void main(String[] args) throws CloneNotSupportedException {
		Employee john = new Employee("John Doe", new BigDecimal(7000.0), 1988, 12, 15);
		Employee evilJohn = john.clone();
		evilJohn.mutabilityTest = 666;
		System.out.println(john);
		System.out.println(evilJohn);
		System.out.println(john == evilJohn);
	}

}
