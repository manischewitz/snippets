package main.org.orgname.objects;


/**
 Immutable
 CREATE TABLE Person(
 	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
 	first_name VARCHAR(36) NOT NULL,
 	last_name VARCHAR(36) NOT NULL,
 	role VARCHAR(128) NOT NULL DEFAULT 'STUDENT'
 	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
public class Person {

	private final long id;
	private final String firstName;
	private final String lastName;
	private final Role role;
	
	public Person(long id, String firstName, String lastName, Role role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}

	public String getFullName() {
		return firstName+" "+lastName;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Role getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (role != other.role)
			return false;
		return true;
	}
	
}
