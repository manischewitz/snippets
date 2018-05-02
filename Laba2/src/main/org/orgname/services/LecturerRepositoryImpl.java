package main.org.orgname.services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.org.orgname.objects.Lecturer;
import main.org.orgname.objects.Person;
import main.org.orgname.objects.Role;
import main.org.orgname.objects.Subject;

@Component
public class LecturerRepositoryImpl implements LecturerRepository {

	private static final String FETCH_ALL_LECTURERS = 
			"SELECT l.person_id,l.subject_id,p.first_name,p.last_name,p.role,s.title " + 
			"FROM LECTURER AS l INNER JOIN PERSON AS p ON p.id=l.person_id" + 
			" INNER JOIN SUBJECT AS s ON s.id=l.subject_id;";
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public List<Lecturer> fetchAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(FETCH_ALL_LECTURERS);
			resultSet = preparedStatement.executeQuery();
			List<Lecturer> list = new ArrayList<>();
			while(resultSet.next()){ 
				long personId = resultSet.getLong("person_id");
				String fname = resultSet.getString("first_name");
				String lname = resultSet.getString("last_name");
				Role role = Enum.valueOf(Role.class, resultSet.getString("role"));
				Person person = new Person(personId, fname, lname, role);
				long subjectId = resultSet.getLong("subject_id");
				String subjectTile = resultSet.getString("title");
				Subject subject = new Subject(subjectTile, subjectId);
				Lecturer lecturer = new Lecturer(person, subject);
				list.add(lecturer);
			}
			return list;
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw sqle;
		}finally{
			try{
				if(resultSet != null) resultSet.close(); 
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
	}

}
