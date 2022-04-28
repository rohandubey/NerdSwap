package com.diprobet.bookSharing.service;

import java.time.LocalDate;
import java.util.List;

import com.diprobet.bookSharing.dto.AllTransactionsReport;
import com.diprobet.bookSharing.dto.BookContribution;
import com.diprobet.bookSharing.dto.Review;
import com.diprobet.bookSharing.entity.Book;
import com.diprobet.bookSharing.entity.Transaction;
import com.diprobet.bookSharing.entity.User;

public interface BookService {

	List<Book> allBookList();

	List<Book> allBookList(int userId, boolean excludeOwner);

	boolean isBookRequestedBy(int userId, int bookId);

	void deleteBook(int bookId);

	void updateBook(Book book);

	Book findBook(int bookId);

	List<BookContribution> topSharedBooks(int howMany);

	void uploadBook(Book book);

	void requestBook(Book book, User user);

	void shareBook(int transactionId, String status);

	void delectBook(int bookid);

	List<Transaction> listOfIndividualBooksRequest(int bookId);

	List<Transaction> requestedBookListOfIndividualUser(int userId);

	void insertReview(Review review);

	List<Review> reviewList(int bookId);

	double findBookRating(int bookId);

	Transaction findTransaction(int transactionId);

	List<Book> searchBook(String param);

	void returnBook(int transactionId, String status, LocalDate returnDate);

	List<AllTransactionsReport> allTransactions();
}
