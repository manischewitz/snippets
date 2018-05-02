package main.org.orgname.objects;

/**
 Immutable
 <p>
 CREATE TABLE groups(
  	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  	group_name VARCHAR(128) NOT NULL UNIQUE,
  	edu_plan_id BIGINT NOT NULL,
  	FOREIGN KEY (edu_plan_id) REFERENCES Education_plan(id)
  ) 	ENGINE=InnoDB  DEFAULT CHARSET=utf8;
  <p>
*/
public class Group {

	private final long id;
	private final String name;
	private final EducationPlan plan;
	
	public Group(long id, String name, EducationPlan plan) {
		this.id = id;
		this.name = name;
		this.plan = plan;
	}
	
	public String getName() {
		return name;
	}
	
	public EducationPlan getPlan() {
		return plan;
	}
	
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", plan=" + plan + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((plan == null) ? 0 : plan.hashCode());
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
		Group other = (Group) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (plan == null) {
			if (other.plan != null)
				return false;
		} else if (!plan.equals(other.plan))
			return false;
		return true;
	}
	
}
