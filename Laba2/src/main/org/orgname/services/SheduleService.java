package main.org.orgname.services;


import java.util.ArrayList;
import java.util.List;


public interface SheduleService {

	public List<String> sheduleFor(int semester) throws Exception;
	
/*public static void main(String...strings) {
		
		final Subject toe = new Subject("toe", 1);
		final Subject progr = new Subject("progr", 2);
		final Subject math = new Subject("math", 3);
		final Subject geom = new Subject("geom", 4);
		List<Lecturer> list = new ArrayList<Lecturer>() {
			private static final long serialVersionUID = 1L;
			{
				super.add(new Lecturer(new Person(), toe));
				super.add(new Lecturer(new Person(), toe));
				super.add(new Lecturer(new Person(), progr));
				super.add(new Lecturer(new Person(), math));
				super.add(new Lecturer(new Person(), geom));
			}
		};  
		
		final PlanSubject pstoe1 = new PlanSubject(0, 1, toe, 40);	
		final PlanSubject pstoe2 = new PlanSubject(0, 1, toe, 20);
		final PlanSubject progr1 = new PlanSubject(0, 1, progr, 20);
		final PlanSubject progr2 = new PlanSubject(0, 1, progr, 30);
		final PlanSubject math1 = new PlanSubject(0, 1, math, 20);
		final PlanSubject math2 = new PlanSubject(0, 1, math, 20);
		//final PlanSubject geom1 = new PlanSubject(0, 1, toe, 60); - will throw ex
		final PlanSubject geom1 = new PlanSubject(0, 1, geom, 40);
		
		EducationPlan eduPlan1 = new EducationPlan(0, "comp sci", 
				new ArrayList<PlanSubject>() {{add(pstoe1);add(progr1);add(math1);add(geom1);}});
		EducationPlan eduPlan2 = new EducationPlan(0, "liberal arts", 
				new ArrayList<PlanSubject>() {{add(pstoe2);add(progr2);add(math2);}});
		
		Group group1 = new Group(1, "group 1",eduPlan1 );
		Group group2 = new Group(1, "group 2",eduPlan2 );
		List<Group> groupList = new ArrayList<>();
		groupList.add(group1);
		groupList.add(group2);
		
		
		
	}*/
	
}
