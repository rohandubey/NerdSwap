package com.diprobet.bookSharing.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.diprobet.bookSharing.dto.AllTransactionsReport;
import com.diprobet.bookSharing.dto.BookContribution;
import com.diprobet.bookSharing.dto.Review;
import com.diprobet.bookSharing.entity.Book;
import com.diprobet.bookSharing.entity.Transaction;
import com.diprobet.bookSharing.entity.User;
import com.diprobet.bookSharing.util.AllTransactionRowMapper;
import com.diprobet.bookSharing.util.BookRowMapper;
import com.diprobet.bookSharing.util.ReviewRowMapper;
import com.diprobet.bookSharing.util.TopSharedBookRowMapper;
import com.diprobet.bookSharing.util.TransactionBookRowMapper;
import com.diprobet.bookSharing.util.TransactionRowMapper;

@Repository
public class BookDaoImpl implements BookDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Book> allBookList() {
		List<Book> bookList = jdbcTemplate.query("SELECT * FROM book", new BookRowMapper());
		return bookList;
	}

	@Override
	public List<Book> allBookList(int userId, boolean excludeOwner) {
		if (excludeOwner) {
			return jdbcTemplate.query("SELECT * FROM book WHERE uploaderId <> ? AND bookStatus = 0",
					new Object[] { userId }, new BookRowMapper());
		} else {
			return jdbcTemplate.query("SELECT * FROM book WHERE uploaderId = ? AND bookStatus = 0",
					new Object[] { userId }, new BookRowMapper());
		}
	}

	@Override
	public void deleteBook(int bookId) {
		jdbcTemplate.update("DELETE FROM book WHERE bookId = ?", new Object[] { bookId });
	}

	@Override
	public void updateBook(Book book) {
		try {
			LobHandler lobHandler = new DefaultLobHandler();
			String sql = "UPDATE book SET uploaderId = ? , bookName = ? ,bookEdition = ? , bookPublication = ? "
					+ ",bookAuthor = ? , bookCategory = ? , coverPhotoOfBookData = ? , bookData = ? WHERE bookId = ?";

			jdbcTemplate.update(sql,
					new Object[] { book.getUploaderId(), book.getBookName(), book.getBookEdition(),
							book.getBookPublication(), book.getBookAuthor(), book.getBookCategory(),
							new SqlLobValue(book.getUploadedCoverPhotoOfBook().getInputStream(),
									(int) book.getUploadedCoverPhotoOfBook().getSize(), lobHandler),
							new SqlLobValue(book.getUploadedBook().getInputStream(),
									(int) book.getUploadedBook().getSize(), lobHandler),
							book.getBookId() },
					new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.BLOB, Types.BLOB, Types.VARCHAR });
			System.out.println("Updated book is " + book.getBookName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Book findBook(int bookId) {
		try {
			String query = "SELECT * FROM book WHERE bookId = ?";
			return jdbcTemplate.queryForObject(query, new BookRowMapper(), bookId);
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public List<BookContribution> topSharedBooks(int howMany) {
		String sql = "SELECT bs.bookName bookName, bs.edition edition, bs.publication publication, bs.author author,"
				+ "u.fullName fullName, bs.sharedCount sharedCount FROM ( SELECT b.bookName bookName, b.bookEdition edition,"
				+ " b.bookPublication publication, b.bookAuthor author, t.ownerId ownerId,"
				+ " COUNT(t.transactionId) sharedCount FROM transaction t JOIN BOOK b ON (t.bookId = b.bookId) WHERE "
				+ "t.status = 'Approve' GROUP BY b.bookId ORDER BY COUNT(b.bookId) DESC ) bs JOIN user u ON (bs.ownerId = u.userId) LIMIT ?";

		return jdbcTemplate.query(sql, new Object[] { howMany }, new TopSharedBookRowMapper());
	}

	@Override
	public void uploadBook(Book book) {
		try {
			LobHandler lobHandler = new DefaultLobHandler();

			String sql = "INSERT INTO book(uploaderId, bookName, bookEdition, bookPublication, bookAuthor, bookCategory,"
					+ " coverPhotoOfBookData, bookData)" + " VALUES ( ? , ? , ? , ? , ? , ? , ?, ?)";

			jdbcTemplate.update(sql,
					new Object[] { book.getUploaderId(), book.getBookName(), book.getBookEdition(),
							book.getBookPublication(), book.getBookAuthor(), book.getBookCategory(),
							new SqlLobValue(book.getUploadedCoverPhotoOfBook().getInputStream(),
									(int) book.getUploadedCoverPhotoOfBook().getSize(), lobHandler),
							new SqlLobValue(book.getUploadedBook().getInputStream(),
									(int) book.getUploadedBook().getSize(), lobHandler) },
					new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.BLOB, Types.BLOB });

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void requestBook(Book book, User user) {
		LocalDate date = LocalDate.now();

		jdbcTemplate.update(
				"INSERT INTO transaction(requestedById,ownerId,bookId,status,date)" + "VALUES (? , ? , ? , ? , ?)",
				user.getUserId(), book.getUploaderId(), book.getBookId(), "Pending", date.plusDays(21));
	}

	@Override
	public void shareBook(int transactionId, String status) {
		String sql = "UPDATE transaction SET status = ? WHERE transactionId = ?";
		jdbcTemplate.update(sql, new Object[] { status, transactionId });
		String sql2 = "UPDATE book INNER JOIN transaction ON (transaction.bookId=book.bookId) SET bookStatus = ? WHERE transaction.transactionId = ?";
		if (status.equals("Approve")) {
			jdbcTemplate.update(sql2, new Object[] { 1, transactionId });
		} else {
			jdbcTemplate.update(sql2, new Object[] { 0, transactionId });
		}
	}

	@Override
	public void returnBook(int transactionId, String status, LocalDate returnDate) {
		String sql = "UPDATE transaction SET status = ? WHERE transactionId = ?";
		jdbcTemplate.update(sql, new Object[] { status, transactionId });
		String sql2 = "UPDATE book INNER JOIN transaction ON (transaction.bookId=book.bookId) SET bookStatus = ? WHERE transaction.transactionId = ?";
		if (status.equals("Complete")) {
			jdbcTemplate.update(sql2, new Object[] { 0, transactionId });
		}
		String sql3 = "UPDATE user INNER JOIN transaction ON (transaction.requestedById=user.userId) SET user.userBalance = user.userBalance-? WHERE transaction.transactionId = ?";
		LocalDate today = LocalDate.now();
		if (today.isAfter(returnDate)) {
			jdbcTemplate.update(sql3, new Object[] { 10, transactionId });
		} else {
			String sql4 = "UPDATE user INNER JOIN transaction ON (transaction.requestedById=user.userId) SET transaction.date = ? WHERE transaction.transactionId = ?";
			jdbcTemplate.update(sql4, new Object[] { today, transactionId });
		}
	}

	public Transaction findTransaction(int transactionId) {
		String sql = "SELECT t.transactionId, t.bookId, own.userId ownerId, own.fullName ownerFullName,"
				+ " own.userName ownerUserName, requestedBy.userId requestedById, requestedBy.fullName  requestedByFullName,"
				+ " requestedBy.userName requestedByUserName, t.date date, t.status FROM transaction t JOIN user requestedBy ON (t.requestedById "
				+ " = requestedBy.userId) JOIN user own ON (t.ownerId = own.userId) WHERE t.transactionId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { transactionId }, new TransactionRowMapper());
	}

	@Override
	public List<Transaction> listOfIndividualBooksRequest(int bookId) {
		String sql = "SELECT t.transactionId, t.bookId, own.userId ownerId, own.fullName ownerFullName,"
				+ " own.userName ownerUserName, requestedBy.userId requestedById, requestedBy.fullName  requestedByFullName,"
				+ " requestedBy.userName requestedByUserName, t.date date, t.status FROM transaction t JOIN user requestedBy ON (t.requestedById "
				+ " = requestedBy.userId) JOIN user own ON (t.ownerId = own.userId) WHERE t.bookId = ? AND t.status = ?";

		return jdbcTemplate.query(sql, new Object[] { bookId, "Pending" }, new TransactionRowMapper());
	}

	@Override
	public List<Transaction> requestedBookListOfIndividualUser(int userId) {
		String sql = "SELECT t.transactionId, t.bookId, own.userId ownerId, own.fullName ownerFullName,"
				+ " own.userName ownerUserName, requestedBy.userId requestedById, requestedBy.fullName  requestedByFullName,"
				+ " requestedBy.userName requestedByUserName, bk.bookName bookName, bk.bookAuthor bookAuthor,"
				+ " bk.bookData bookPdf, t.date date, t.status FROM transaction t JOIN user requestedBy ON"
				+ " (t.requestedById  = requestedBy.userId) JOIN user own ON (t.ownerId = own.userId) JOIN book bk ON"
				+ "(t.bookId = bk.bookId) WHERE requestedBy.userId = ?";
		return jdbcTemplate.query(sql, new Object[] { userId }, new TransactionBookRowMapper());

	}

	@Override
	public boolean isBookRequestedBy(int userId, int bookId) {
		String sql = "SELECT count(t.transactionId) FROM transaction t WHERE t.requestedById = ? AND t.bookId = ?";

		int transactionCount = jdbcTemplate.queryForObject(sql, new Object[] { userId, bookId }, Integer.class);

		return transactionCount > 0;
	}

	@Override
	public void insertReview(Review review) {
		String sql = "INSERT INTO `review`(reviewedById, bookId, review, rating) VALUES (?,?,?, ?)";
		jdbcTemplate.update(sql,
				new Object[] { review.getReviewedById(), review.getBookId(), review.getReview(), review.getRating() });
	}

	@Override
	public List<Review> reviewList(int bookId) {
		String sql = "SELECT u.fullName fullName, r.review review, r.rating rating FROM user u JOIN review r ON (u.userId = r.reviewedById) WHERE bookId = ?";
		List<Review> bookList = jdbcTemplate.query(sql, new Object[] { bookId }, new ReviewRowMapper());
		return bookList;
	}

	@Override
	public double findBookRating(int bookId) {
		String sql = "SELECT AVG(rating) rating FROM review WHERE bookId = ?";
		Double rating = (jdbcTemplate.queryForObject(sql, new Object[] { bookId }, Double.class));

		return (rating == null) ? 0.0 : rating;
	}

	@Override
	public List<Book> searchBook(String param) {
		String sql = "SELECT bookId, bookName  FROM book WHERE bookName LIKE ?";
		return jdbcTemplate.query(sql, new Object[] { "%" + param + "%" }, new RowMapper<Book>() {
			@Override
			public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Book book = new Book();

				book.setBookId(resultSet.getInt("bookId"));
				book.setBookName(resultSet.getString("bookName"));

				return book;
			}
		});
	}

	@Override
	public List<AllTransactionsReport> allTransactions() {
		String sql = "SELECT transaction.transactionId, u1.fullName as \"borrower\",u2.fullName as \"owner\", book.bookName, transaction.status, transaction.date \n"
				+ "FROM transaction\n" + "LEFT JOIN  book ON (book.bookId = transaction.bookId )\n"
				+ "LEFT JOIN user AS u1 ON (u1.userId = transaction.requestedById)\n"
				+ "LEFT JOIN user as u2 ON (u2.userId = transaction.ownerId);";

		return jdbcTemplate.query(sql, new Object[] {}, new AllTransactionRowMapper());
	}

}
