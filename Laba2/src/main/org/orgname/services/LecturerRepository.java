package main.org.orgname.services;


import java.sql.SQLException;
import java.util.List;

import main.org.orgname.objects.Lecturer;


public interface LecturerRepository {

	public List<Lecturer> fetchAll() throws SQLException;
	
}
