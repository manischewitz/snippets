package main.org.orgname.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import main.org.orgname.services.SheduleService;


@RestController
public class RESTSheduleServiceController {

	@Autowired
	private SheduleService sheduleService;
	
	@RequestMapping(value="/getShedule/{semester}", method=RequestMethod.GET)
	public List<String> readQuestion(@PathVariable String semester) {
		try {
			Integer semesterNo = Integer.valueOf(semester);
			return sheduleService.sheduleFor(semesterNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} 
	
}
