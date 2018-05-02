package main.org.orgname.objects;

/**
 Immutable
 CREATE TABLE Lecturer(
 	person_id BIGINT UNSIGNED NOT NULL,
 	subject_id BIGINT UNSIGNED NOT NULL,
 	FOREIGN KEY (person_id) REFERENCES Person(id),
 	FOREIGN KEY (subject_id) REFERENCES Subject(id)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/
public class Lecturer {

	private final Person person;
	private final Subject subject;
	
	public Lecturer(Person person, Subject subject) {
		this.person = person;
		this.subject = subject;
	}

	public Person getPerson() {
		return person;
	}

	public Subject getSubject() {
		return subject;
	}

	@Override
	public String toString() {
		return "Lecturer [person=" + person + ", subject=" + subject + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Lecturer other = (Lecturer) obj;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

}
