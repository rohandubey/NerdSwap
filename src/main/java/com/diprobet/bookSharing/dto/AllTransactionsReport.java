package com.diprobet.bookSharing.dto;

import java.time.LocalDate;

import com.diprobet.bookSharing.entity.Book;
import com.diprobet.bookSharing.entity.Transaction;
import com.diprobet.bookSharing.entity.User;

public class AllTransactionsReport {

	private Transaction transaction;
	private User borrower;
	private User owner;
	private Book book;

	public void setTransactionId(int transactionId) {
		this.transaction.setTransactionId(transactionId);
	}

	public AllTransactionsReport(int transactionId, User borrower, User owner, Book book, String status,
			LocalDate date) {
		super();
		this.transaction.setTransactionId(transactionId);
		this.borrower = borrower;
		this.owner = owner;
		this.book = book;
		this.transaction.setStatus(status);
		this.transaction.setDate(date);
	}

	public AllTransactionsReport() {
		this.borrower = new User();
		this.owner = new User();
		this.book = new Book();
		this.transaction = new Transaction();

	}

	public int getTransactionId() {
		return transaction.getTransactionId();
	}

	public User getBorrower() {
		return borrower;
	}

	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}

	public User getOwner() {
		return owner;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
