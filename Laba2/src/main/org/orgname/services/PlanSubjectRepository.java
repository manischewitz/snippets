package main.org.orgname.services;


import java.sql.SQLException;
import java.util.List;

import main.org.orgname.objects.PlanSubject;


public interface PlanSubjectRepository {

	public List<PlanSubject> fetchAllByEduPlanName(String name, int semester) throws SQLException;

	public List<PlanSubject> fetchAllByEduPlanName(String edu_plan_title) throws SQLException;
	
}
