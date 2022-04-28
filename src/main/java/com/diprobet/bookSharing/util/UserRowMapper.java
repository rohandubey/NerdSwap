package com.diprobet.bookSharing.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.diprobet.bookSharing.entity.User;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet resultSet, int i) throws SQLException {
		User user = new User();

		user.setUserId(resultSet.getInt("userId"));
		user.setFullName(resultSet.getString("fullName"));
		user.setUserName(resultSet.getString("userName"));
		user.setUserPassword(resultSet.getString("userPassword"));
		user.setUserMail(resultSet.getString("userMail"));
		user.setUserContact(resultSet.getString("userContact"));
		user.setUserSecurityAns(resultSet.getString("userSecurityAns"));
		user.setUserType(resultSet.getString("userType"));
		user.setUserBalance(resultSet.getInt("userBalance"));

		return user;
	}
}
