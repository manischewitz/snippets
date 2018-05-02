
public class Person implements Comparable<Person> {

	private long id;
	private String firstName;
	
	public Person(long id, String firstName) {
		this.id = id;
		this.firstName = firstName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + "]";
	}

	@Override
	public int compareTo(Person other) {
		if (other.getId() == id) {
			return 0;
		} else if (other.getId() > id) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
