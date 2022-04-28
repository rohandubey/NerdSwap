package com.diprobet.bookSharing.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.diprobet.bookSharing.entity.Transaction;
import com.diprobet.bookSharing.entity.User;
import com.diprobet.bookSharing.service.BookService;

@Controller
@RequestMapping(value = "/requestedBookListOfIndividual")
public class RequestedBookListController {

	@Autowired
	private BookService bookService;

	@RequestMapping(method = RequestMethod.GET)
	public String requestedBookList(ModelMap modelMap, HttpSession session) {

		User user = (User) session.getAttribute("currentUser");

		List<Transaction> List = bookService.requestedBookListOfIndividualUser(user.getUserId());

		modelMap.put("user", user);

		modelMap.put("list", List);

		return "requestedBookListOfIndividualUser";
	}

}
