package main.org.orgname.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.org.orgname.objects.Group;
import main.org.orgname.objects.Lecturer;
import main.org.orgname.objects.PlanSubject;
import main.org.orgname.objects.Subject;

@Component
public class SheduleServiceImpl implements SheduleService {

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private LecturerRepository lecturerRepository;
	
	private class Class {

		List<Group> groups;
		int numberOfClasses;
		Lecturer lecturer;
		
		@Override
		public String toString() {
			return "Class [groups=" + groups + ", numberOfClasses=" + numberOfClasses + ", lecturer=" + lecturer + "]";
		}
	}
	
	@Override
	public List<String> sheduleFor(int semester) throws Exception {
		final List<Lecturer> lecturerList = lecturerRepository.fetchAll();
		final List<Group> groupList = groupRepository.fetchAll(semester);
		
		final Map<Subject, List<Lecturer>> subL = new HashMap<>();
		
		lecturerList.forEach((Lecturer lecturer) -> {
			Subject subject = lecturer.getSubject();
			if (subL.containsKey(subject)) {
				subL.get(subject).add(lecturer);
			} else {
				List<Lecturer> l = new ArrayList<>();
				l.add(lecturer);
				subL.put(subject, l);
			}
		});
		
		final Map<Lecturer, List<Group>> pairs = new HashMap<>();
		
		subL.forEach((Subject s, List<Lecturer> l) -> {
			groupList.forEach((Group group) -> {
				group.getPlan().getSubjectList().forEach((PlanSubject ps) -> {
					if (ps.getSubjects().equals(s)) {
						Lecturer le = findLecturer(l, ps, pairs);
						if (pairs.containsKey(le)) {
							pairs.get(le).add(group);
						} else {
							List<Group> list1 = new ArrayList<>();
							list1.add(group);
							pairs.put(le, list1);
						}
					}
				});
			});
		});
		List<String> shedule = new ArrayList<>();
		groupList.forEach((Group group) -> {
			shedule.add(sheduleFor(group, formClasses(pairs)));
		});
		return shedule;
	}

	private String sheduleFor(Group group, List<Class> classes) {
		StringBuffer sb = new StringBuffer();
		sb.append(group.getName());
		sb.append(' ');
		sb.append(group.getPlan().getTitle());
		sb.append('\n');
			
			for (int week = 1; week < 21; week++) {
				for (int day = 1; day < 7; day++) {
					Iterator<Class> i = classes.iterator();
					int p = 0;
						while (i.hasNext()) {
							Class c = i.next();
							if (c.numberOfClasses == 0) {
								break;
							}
							p++;
							int classesPerWeek = c.numberOfClasses / 21;
							if (classesPerWeek == 0) {
								classesPerWeek = 1;
							}
							if(c.groups.contains(group)) {
								c.numberOfClasses--;
								sb.append(week);
								sb.append(" неделя, день ");
								sb.append(day);
								sb.append(" пара ");
								sb.append(p);
								sb.append(' ');
								sb.append(c.lecturer.getSubject().getTitle());
								sb.append(' ');
								sb.append(c.lecturer.getPerson().getFullName());
								sb.append('\n');
							}
						}
					}
				
				sb.append('\n');
			}
		return sb.toString();
	}
	
	private List<Class> formClasses(Map<Lecturer, List<Group>> pairs) {
		List<Class> classes = new ArrayList<>();
		
		pairs.forEach((Lecturer l, List<Group> list) -> {
			Iterator<Group> i = list.iterator();
			Map<Integer, List<Group>> groupsWithEqualNclasses = new HashMap<>();
			while (i.hasNext()) {
				Group group = i.next();
				PlanSubject ps = findAppropriateSubject(group.getPlan().getSubjectList(), l.getSubject());
				
				if (groupsWithEqualNclasses.containsKey(ps.getNumberOfClasses())) {
					groupsWithEqualNclasses.get(ps.getNumberOfClasses()).add(group);
				} else {
					List<Group> list1 = new ArrayList<>();
					list1.add(group);
					groupsWithEqualNclasses.put(ps.getNumberOfClasses(), list1);
				}
			}
			
			groupsWithEqualNclasses.forEach((Integer nClasses, List<Group> groups) -> {
				Class newClass = new Class();
				newClass.lecturer = l;
				newClass.numberOfClasses = nClasses;
				newClass.groups = groups;
				classes.add(newClass);
			});
			
		});
		
		return classes;
	}

	private PlanSubject findAppropriateSubject (List<PlanSubject> subjects, Subject s) {
		Iterator<PlanSubject> k = subjects.iterator();
		while (k.hasNext()) {
			PlanSubject ps = k.next();
			if (s.equals(ps.getSubjects())) {
				return ps;
			}
		}
		throw new IllegalArgumentException("never happens");
	}

	private Lecturer findLecturer(List<Lecturer> l, PlanSubject ps, Map<Lecturer, List<Group>> pairs) {
		Lecturer free = null;
		Iterator<Lecturer> i = l.iterator();
		while (i.hasNext()) {
			Lecturer lecturer = i.next();
			if (!pairs.containsKey(lecturer)) {
				free = lecturer;
				break;
			}
		}
		if (free == null) { // свободных нет 
			Set<Lecturer> lectL = pairs.keySet();
			Iterator<Lecturer> it = lectL.iterator();
			while (it.hasNext()) {
				Lecturer lec = it.next();
				if (lec.getSubject().equals(ps.getSubjects())) {
					free = lec;
				}
			}
			if (free == null) {
				throw new IllegalStateException("Нет преподавателя по этому предмету");
			}
		} 
		return free;
	}

	
}
