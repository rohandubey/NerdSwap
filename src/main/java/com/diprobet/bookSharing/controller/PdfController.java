package com.diprobet.bookSharing.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diprobet.bookSharing.entity.Book;
import com.diprobet.bookSharing.service.BookService;

@Controller
@RequestMapping(value = "/pdf")
public class PdfController {

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void downloadPdf(@RequestParam("bookId") int bookId, HttpServletResponse response) throws IOException {

		Book book = bookService.findBook(bookId);

		InputStream inputStream = new ByteArrayInputStream(book.getBookData());
		response.setContentType("application/pdf");

		response.setHeader("Content - Disposition", "attachment; filename = \"" + book.getBookData() + ".pdf\"");
		IOUtils.copy(inputStream, response.getOutputStream());
	}

}
