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

import main.org.orgname.objects.PlanSubject;
import main.org.orgname.objects.Subject;

@Component
public class PlanSubjectRepositoryImpl implements PlanSubjectRepository {

	private static final String FETCH_BY_SUBJECT_NAME = 
			"SELECT p.id,p.number_of_classes,s.id AS subject_id,s.title FROM PLAN AS p" + 
			" INNER JOIN EDUCATION_PLAN AS ep ON p.education_plan_id=ep.id " + 
			" INNER JOIN SUBJECT AS s ON p.subject_id=s.id" + 
			" WHERE ep.title=? AND p.semestr_no=?;";
	
	private static final String FETCH_BY_SUBJECT_NAME_FOR_ALL_SEMESTER = 
			"SELECT p.id,p.number_of_classes,s.id  AS subject_id,s.title, p.semestr_no FROM PLAN AS p" + 
			" INNER JOIN EDUCATION_PLAN AS ep ON p.education_plan_id=ep.id " + 
			" INNER JOIN SUBJECT AS s ON p.subject_id=s.id" + 
			" WHERE ep.title=?;";
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public List<PlanSubject> fetchAllByEduPlanName(String name, int semester) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(FETCH_BY_SUBJECT_NAME);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, semester);
			resultSet = preparedStatement.executeQuery();
			List<PlanSubject> list = new ArrayList<>();
			while(resultSet.next()){ 
				long subjectId = resultSet.getLong("subject_id");
				String title = resultSet.getString("title");
				Subject subject = new Subject(title, subjectId);
				long planId = resultSet.getLong("id");
				int numberOfClasses = resultSet.getInt("number_of_classes");
				PlanSubject planSubject = new PlanSubject(planId, semester, subject, numberOfClasses);
				list.add(planSubject);
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

	@Override
	public List<PlanSubject> fetchAllByEduPlanName(String edu_plan_title) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(FETCH_BY_SUBJECT_NAME_FOR_ALL_SEMESTER);
			preparedStatement.setString(1, edu_plan_title);
			resultSet = preparedStatement.executeQuery();
			List<PlanSubject> list = new ArrayList<>();
			while(resultSet.next()){ 
				long subjectId = resultSet.getLong("subject_id");
				String title = resultSet.getString("title");
				Subject subject = new Subject(title, subjectId);
				long planId = resultSet.getLong("id");
				int semester = resultSet.getInt("semestr_no");
				int numberOfClasses = resultSet.getInt("number_of_classes");
				PlanSubject planSubject = new PlanSubject(planId, semester, subject, numberOfClasses);
				list.add(planSubject);
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
