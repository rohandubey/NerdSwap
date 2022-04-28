package com.diprobet.bookSharing.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diprobet.bookSharing.entity.User;
import com.diprobet.bookSharing.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public String showUserList(ModelMap modelMap) {
		List<User> list = userService.allUserList();

		modelMap.put("allUserList", list);

		return "userList";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("userId") int userId) {
		userService.deleteUser(userId);

		return "redirect:http://localhost:8080/user/userList";
	}

	private void setupReferenceData(ModelMap modelMap) {
		modelMap.put("userTypeOptionList", new ArrayList<>(Arrays.asList("Admin", "User")));
		modelMap.put("bookTypeOptionList", new ArrayList<>(Arrays.asList("Academic", "Biography", "Textbook", "Fantasy",
				"Science fiction", "Thriller", "Poetry", "Mythology")));
	}

}
