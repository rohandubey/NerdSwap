package com.diprobet.bookSharing.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.diprobet.bookSharing.dto.AllTransactionsReport;

public class AllTransactionRowMapper implements RowMapper<AllTransactionsReport> {

	@Override
	public AllTransactionsReport mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		AllTransactionsReport transactions = new AllTransactionsReport();
		transactions.getOwner().setFullName(resultSet.getString("owner"));
		transactions.getBorrower().setFullName(resultSet.getString("borrower"));
		transactions.getBook().setBookName(resultSet.getString("bookName"));
		transactions.getTransaction().setTransactionId(resultSet.getInt("transactionId"));
		transactions.getTransaction().setStatus(resultSet.getString("status"));
		transactions.getTransaction().setDate(resultSet.getObject("date", LocalDate.class));

		return transactions;
	}
}
