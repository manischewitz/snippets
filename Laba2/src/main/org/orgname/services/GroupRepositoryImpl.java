package main.org.orgname.services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.org.orgname.objects.EducationPlan;
import main.org.orgname.objects.Group;
import main.org.orgname.objects.PlanSubject;

@Component
public class GroupRepositoryImpl implements GroupRepository {
	
	private static final String SELECT_BY_NAME_WITH_TITLE = 
			"SELECT g.id, g.group_name, ep.title, ep.id AS ep_id FROM GROUPS AS g "
			+ "INNER JOIN EDUCATION_PLAN AS ep ON g.edu_plan_id=ep.id WHERE g.group_name=?;";
	
	private static final String SELECT_ALL_WITH_SPECIFIED_SEMESTER = 
			"SELECT g.id, g.group_name, ep.title, ep.id AS ep_id FROM GROUPS AS g "
			+ "INNER JOIN EDUCATION_PLAN AS ep ON g.edu_plan_id=ep.id;";

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlanSubjectRepository planSubjectRepository;
	
	@Override
	public Group fetchByName(String name, int forSemester) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(SELECT_BY_NAME_WITH_TITLE);
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();
			Group group = null;
			if(resultSet.next()){
				long edu_plan_id = resultSet.getLong("ep_id");
				String edu_plan_title = resultSet.getString("title");
				List<PlanSubject> listOfSubjects = planSubjectRepository.
						fetchAllByEduPlanName(edu_plan_title, forSemester);
				EducationPlan educationPlan = 
						new EducationPlan(edu_plan_id, edu_plan_title, listOfSubjects);
				long groupId = resultSet.getLong("id");
				group = new Group(groupId, name, educationPlan);
			}
			if (group == null) throw new NoSuchElementException();
			return group;
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
	public List<Group> fetchAll(int forSemester) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(SELECT_ALL_WITH_SPECIFIED_SEMESTER );
			resultSet = preparedStatement.executeQuery();
			List<Group> groups = new ArrayList<>();
			while(resultSet.next()){
				long edu_plan_id = resultSet.getLong("ep_id");
				String edu_plan_title = resultSet.getString("title");
				List<PlanSubject> listOfSubjects;
				if (forSemester > 0) {
					listOfSubjects = planSubjectRepository.
							fetchAllByEduPlanName(edu_plan_title, forSemester);
				} else {
					listOfSubjects = planSubjectRepository.
							fetchAllByEduPlanName(edu_plan_title);
				}
				EducationPlan educationPlan = 
						new EducationPlan(edu_plan_id, edu_plan_title, listOfSubjects);
				long groupId = resultSet.getLong("id");
				String name = resultSet.getString("group_name");
				Group group = new Group(groupId, name, educationPlan);
				groups.add(group);
			}
			return groups;
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
