import java.math.BigDecimal;

public class Employee extends Person {

	private BigDecimal salary;
	
	public Employee(long id, String firstName) {
		super(id, firstName);
	}
	
	private void extendTest() {
		Pair<Person> person = new Pair<>();
		person.setFirst(new Person(1, "John Doe"));
		person.setSecond(new Person(2, "Anna Lee"));
		Pair<? extends Person> other = person;
		//other.setSecond(new Employee(3, "Foo Bar")); error!
		other.getFirst();
		//other.setSecond(new Person(3, "Foo Bar")); error!
	}
	
	private void superTest(Pair<? super Employee> pair) {
		pair.setFirst(new Employee(1, "John Doe"));
		//pair.setFirst(new Person(1, "John Doe")); error!
		Pair<? super Person> pair2 = new Pair<>();
		pair2.setFirst(new Employee(1, "John Doe"));
		//pair2.setFirst(new Object()); error!
		pair2.getFirst();
		pair.getSecond();
	}

}
