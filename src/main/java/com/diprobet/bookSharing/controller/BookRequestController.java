package com.diprobet.bookSharing.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diprobet.bookSharing.dto.Notification;
import com.diprobet.bookSharing.entity.Book;
import com.diprobet.bookSharing.entity.User;
import com.diprobet.bookSharing.service.BookService;
import com.diprobet.bookSharing.service.UserService;

@Controller
@RequestMapping("request")
public class BookRequestController {

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String requestForBook(@RequestParam("bookId") int bookId, HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		Book book = bookService.findBook(bookId);
		bookService.requestBook(book, user);
		if (user.getUserBalance() > 10) {
			sendNotification(user, book);
		} else {
			sendNotification2(user, book);
		}
		return "redirect:http://localhost:8080/book/bookList?userId=" + user.getUserId() + "&excludeOwner=true";
	}

	private void sendNotification(User user, Book book) {
		String message = user.getFullName() + " requested  for " + book.getBookName();
		User notifiedTo = userService.findUser(book.getUploaderId());
		Notification notification = new Notification(notifiedTo, book, "Request", message);
		userService.sendNotification(notification);
	}

	private void sendNotification2(User user, Book book) {
		String message = "Balance low. Minimum 10 credits needed.";
		Notification notification = new Notification(user, book, "Request", message);
		userService.sendNotification(notification);
	}

}
