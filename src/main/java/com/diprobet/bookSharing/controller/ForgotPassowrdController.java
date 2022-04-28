package com.diprobet.bookSharing.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.diprobet.bookSharing.entity.User;
import com.diprobet.bookSharing.service.UserService;

@Controller
public class ForgotPassowrdController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
	public String signUpFormShow(ModelMap modelMap) {
		User user = new User();
		modelMap.put("user", user);

		return "forgotPasswordForm";
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	public String save(@ModelAttribute User user) {
		user.setUserType("User");
		userService.userReRegistration(user);
		return "redirect:http://localhost:8080/login";
	}

	private void setupReferenceData(ModelMap modelMap) {
		modelMap.put("userTypeOptionList", new ArrayList<>(Arrays.asList("Admin", "User")));
		modelMap.put("bookTypeOptionList",
				new ArrayList<>(Arrays.asList("Academic", "Science", "Mathematics", "Business", "Others")));
	}

}
