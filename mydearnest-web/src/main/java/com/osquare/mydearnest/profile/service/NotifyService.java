package com.osquare.mydearnest.profile.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Notification;


public interface NotifyService {

	Collection<Notification> getNotificationsByLastId(long last_id);
	Collection<Notification> getNotificationsByLastIdAndFollower(long lastId, long accountId);

	Long getNotifyHistoryCount(Account account);
	Long getNotifyMessageCount(Account account);
	Collection<Notification> getHistoryByAccountId(Long accountId, int page);


}
