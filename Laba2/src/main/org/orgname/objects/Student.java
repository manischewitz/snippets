package main.org.orgname.objects;


/**Immutable
 CREATE TABLE Student(
 	person_id BIGINT UNSIGNED NOT NULL,
 	group_id BIGINT UNSIGNED NOT NULL,
 	FOREIGN KEY (person_id) REFERENCES Person(id),
 	FOREIGN KEY (group_id) REFERENCES Groups(id)
 ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
 */
public class Student {

	private final long groupId;
	private final Person person;
	
	public Student(long groupId, Person person) {
		this.groupId = groupId;
		this.person = person;
	}

	public long getGroupId() {
		return groupId;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public String toString() {
		return "Student [groupId=" + groupId + ", person=" + person + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (groupId ^ (groupId >>> 32));
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		Student other = (Student) obj;
		if (groupId != other.groupId)
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}
	
}
