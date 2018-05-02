package main.org.orgname.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import main.org.orgname.objects.Group;
import main.org.orgname.services.GroupRepository;

@Controller
public class IndexPageController {

	@Autowired
	private GroupRepository groupRepository;
	
	@RequestMapping(value="/*", method=RequestMethod.GET)
	public String showManagementPage(Model model) {
		try {
			List<Group> list = groupRepository.fetchAll(-1);
			System.out.println(Arrays.toString(list.toArray()));
			model.addAttribute("groups", list);
		} catch (Exception e) {
			model.addAttribute("error", "unexpected error");
		}
		return "index";
	}
	
}
