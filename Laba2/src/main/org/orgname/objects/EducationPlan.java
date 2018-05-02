package main.org.orgname.objects;


import java.util.List;
/**
  Immutable
  CREATE TABLE Education_plan(
  	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
 	title VARCHAR(128) UNIQUE NOT NULL
  ) 	ENGINE=InnoDB  DEFAULT CHARSET=utf8;
*/
public class EducationPlan {

	private final long id;
	private final String title;
	private final List<PlanSubject> subjectList;
	
	public EducationPlan(long id, String title, List<PlanSubject> subjectList) {
		this.id = id;
		this.title = title;
		this.subjectList = subjectList;
	}
	
	public String getTitle() {
		return title;
	}
	
	public List<PlanSubject> getSubjectList() {
		return subjectList;
	}
	
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "EducationPlan [id=" + id + ", title=" + title + ", subjectList=" + subjectList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((subjectList == null) ? 0 : subjectList.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		EducationPlan other = (EducationPlan) obj;
		if (id != other.id)
			return false;
		if (subjectList == null) {
			if (other.subjectList != null)
				return false;
		} else if (!subjectList.equals(other.subjectList))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	} 
	
}
