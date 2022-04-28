package com.diprobet.bookSharing.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.diprobet.bookSharing.dto.BookContribution;
import com.diprobet.bookSharing.dto.Notification;
import com.diprobet.bookSharing.entity.User;
import com.diprobet.bookSharing.util.AdminRawMapper;
import com.diprobet.bookSharing.util.BookContributionRowMapper;
import com.diprobet.bookSharing.util.NotificationRowMapper;
import com.diprobet.bookSharing.util.UserRowMapper;

@Repository("UserDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<User> allUserList() {
		List<User> userList = jdbcTemplate.query("SELECT * FROM user", new AdminRawMapper());
		return userList;
	}

	@Override
	public void deleteUser(int userId) {
		jdbcTemplate.update("DELETE FROM user WHERE userId = ?", new Object[] { userId });
	}

	@Override
	public void userRegistration(User user) {
		jdbcTemplate.update(
				"INSERT INTO user(fullname,userName, userPassword, UserMail, userContact, userSecurityAns , userType, userBalance) "
						+ "VALUES (? , ? , ? , ? , ? , ? , ? , ?)",
				user.getFullName(), user.getUserName(), user.getUserPassword(), user.getUserMail(),
				user.getUserContact(), user.getUserSecurityAns(), user.getUserType(), user.getUserBalance());
		System.out.println("Registration Complete for " + user.getUserName());
	}

	@Override
	public void updateUserInformation(User user) {
		jdbcTemplate.update(
				"UPDATE  user  SET fullName = ? , userName = ? , userPassword = ? , userMail = ?, userContact = ? , userType = ?, userBalance = ? WHERE userId = ?",
				user.getFullName(), user.getUserName(), user.getUserPassword(), user.getUserMail(),
				user.getUserContact(), user.getUserType(), user.getUserBalance(), user.getUserId());
	}

	@Override
	public void userReRegistration(User user) {
		List<User> userList = allUserList();
		for (User u : userList) {
//			System.out.println(u.getUserName()+" "+user.getUserName()+u.getUserSecurityAns());
			if (u.getUserName().equals(user.getUserName())
					&& u.getUserSecurityAns().equals(user.getUserSecurityAns())) {
				jdbcTemplate.update(
						"UPDATE  user  SET fullName = ? , userName = ? , userPassword = ? , userMail = ?, userContact = ?, userBalance = ?, userType = ? WHERE userId = ?",
						u.getFullName(), u.getUserName(), user.getUserPassword(), u.getUserMail(), u.getUserContact(),
						u.getUserBalance(), u.getUserType(), u.getUserId());
			}
		}
	}

	@Override
	public User findUser(int userId) {
		try {
			String query = "SELECT * FROM user WHERE userId = ?";
			return jdbcTemplate.queryForObject(query, new Object[] { userId }, new AdminRawMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public User userLoginOrNot(String userName, String userPassword, String userType) {
		try {
			String query = "SELECT * FROM user WHERE userName = ? AND userPassword = ? AND userType = ?";
			return jdbcTemplate.queryForObject(query, new Object[] { userName, userPassword, userType },
					new UserRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public List<BookContribution> topContributor(int howMany) {
		String sql = "SELECT u.fullName fullName, u.userName userName, COUNT(bookId) bookCount FROM user u JOIN book bk ON "
				+ "(u.userId = bk.uploaderId) GROUP BY bk.uploaderId ORDER BY COUNT(bk.uploaderId) DESC LIMIT ?";

		return jdbcTemplate.query(sql, new Object[] { howMany }, new BookContributionRowMapper());
	}

	@Override
	public void sendNotification(Notification notification) {
		String sql = "INSERT INTO notification(massage, acknowledgement, notificationType, notificationTo, bookId)"
				+ " VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql,
				new Object[] { notification.getMessage(), notification.isAcknowledgement(), notification.getType(),
						notification.getNotificationTo().getUserId(), notification.getBook().getBookId() });
	}

	public void notificationAcknowledgement(int notificationId) {
		String sql = "UPDATE notification SET acknowledgement = ? WHERE notificationId = ?";

		jdbcTemplate.update(sql, new Object[] { 1, notificationId });

	}

	public List<Notification> getNotificationList(int userId) {
		String sql = "SELECT  n.notificationId notificationId, n.massage massage, n.notificationType notificationType, n.bookId bookId FROM "
				+ "notification n WHERE n.notificationTo = ? AND n.acknowledgement = ?";

		return jdbcTemplate.query(sql, new Object[] { userId, 0 }, new NotificationRowMapper());
	}

}
