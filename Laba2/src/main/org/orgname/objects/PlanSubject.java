package main.org.orgname.objects;


/**Immutable
 CREATE TABLE Plan(
 	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
 	semestr_no MEDIUMINT UNSIGNED NOT NULL, 
 	education_plan_id BIGINT UNSIGNED NOT NULL,
 	number_of_classes BIGINT UNSIGNED NOT NULL,
 	subject_id BIGINT UNSIGNED NOT NULL,
 	FOREIGN KEY (education_plan_id) REFERENCES Education_plan(id),
 	FOREIGN KEY (subject_id) REFERENCES Subject(id)
 ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
*/
public class PlanSubject {

	private final long id;
	private final int semestr;
	private final Subject subjects;
	private final int numberOfClasses;
	
	public PlanSubject(long id, int semestr, Subject subjects, int numberOfClasses) {
		this.id = id;
		this.semestr = semestr;
		this.subjects = subjects;
		this.numberOfClasses = numberOfClasses;
	}
	
	public long getId() {
		return id;
	}
	
	public int getSemestr() {
		return semestr;
	}
	
	public Subject getSubjects() {
		return subjects;
	}
	
	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	@Override
	public String toString() {
		return "PlanSubject [id=" + id + ", semestr=" + semestr + ", subjects=" + subjects + ", numberOfClasses="
				+ numberOfClasses + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + numberOfClasses;
		result = prime * result + semestr;
		result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
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
		PlanSubject other = (PlanSubject) obj;
		if (id != other.id)
			return false;
		if (numberOfClasses != other.numberOfClasses)
			return false;
		if (semestr != other.semestr)
			return false;
		if (subjects == null) {
			if (other.subjects != null)
				return false;
		} else if (!subjects.equals(other.subjects))
			return false;
		return true;
	}
	
	
}
