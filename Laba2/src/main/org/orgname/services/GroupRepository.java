package main.org.orgname.services;



import java.sql.SQLException;
import java.util.List;

import main.org.orgname.objects.Group;


public interface GroupRepository {

	public Group fetchByName(String name, int forSemester) throws SQLException;
	
	public List<Group> fetchAll(int forSemester) throws SQLException;
	
	
}
