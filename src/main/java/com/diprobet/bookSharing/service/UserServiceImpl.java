package com.diprobet.bookSharing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diprobet.bookSharing.dao.UserDao;
import com.diprobet.bookSharing.dto.BookContribution;
import com.diprobet.bookSharing.dto.Notification;
import com.diprobet.bookSharing.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public List<User> allUserList() {
		return userDao.allUserList();
	}

	@Override
	public void deleteUser(int userId) {
		userDao.deleteUser(userId);
	}

	@Override
	public void userRegistration(User user) {
		userDao.userRegistration(user);
	}

	@Override
	public void userReRegistration(User user) {
		userDao.userReRegistration(user);
	}

	@Override
	public void updateUserInformation(User user) {
		userDao.updateUserInformation(user);
	}

	@Override
	public User findUser(int userId) {
		return userDao.findUser(userId);
	}

	@Override
	public User userLoginOrNot(String userName, String userPassword, String userType) {
		return userDao.userLoginOrNot(userName, userPassword, userType);
	}

	@Override
	public List<BookContribution> topContributor(int howMany) {
		return userDao.topContributor(howMany);
	}

	public void sendNotification(Notification notification) {
		userDao.sendNotification(notification);
	}

	@Override
	public void notificationAcknowledgement(int notificationId) {
		userDao.notificationAcknowledgement(notificationId);
	}

	@Override
	public List<Notification> getNotificationList(int userId) {
		return userDao.getNotificationList(userId);
	}

}
