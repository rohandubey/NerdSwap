package com.diprobet.bookSharing.service;

import java.util.List;

import com.diprobet.bookSharing.dto.BookContribution;
import com.diprobet.bookSharing.dto.Notification;
import com.diprobet.bookSharing.entity.User;

public interface UserService {

	List<User> allUserList();

	void deleteUser(int userId);

	void userRegistration(User user);

	void userReRegistration(User user);

	void updateUserInformation(User user);

	User findUser(int userId);

	User userLoginOrNot(String userName, String userPassword, String userType);

	List<BookContribution> topContributor(int howMany);

	void sendNotification(Notification notification);

	void notificationAcknowledgement(int notificationId);

	List<Notification> getNotificationList(int userId);
}
