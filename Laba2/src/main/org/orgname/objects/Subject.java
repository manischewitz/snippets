package main.org.orgname.objects;


/**
 Immutable
 CREATE TABLE Subject(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(128) NOT NULL
 ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
*/
public class Subject {

	private final String title;
	private final long id;
	
	public Subject(String title, long id) {
		this.title = title;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Subject [title=" + title + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Subject other = (Subject) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
