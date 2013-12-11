package com.osquare.mydearnest.profile.service;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;




import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Notification;

@Service("notifyService")
public class NotifyServiceImpl implements NotifyService {
	
	@Resource private SessionFactory sessionFactory;

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Notification> getNotificationsByLastId(long lastId) {
		
		Collection<Notification> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			
			Query cr = session.getNamedQuery("getNotificationsByLastId")
					.setResultTransformer(Transformers.aliasToBean(Notification.class))
					.setLong("id", lastId);
			
			cr = cr.setMaxResults(30);
			result = cr.list();
			session.getTransaction().commit();
		}
		catch(HibernateException ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Notification> getNotificationsByLastIdAndFollower(long lastId, long accountId) {

		Collection<Notification> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			
			Query cr = session.getNamedQuery("getNotificationsByLastIdWithFollower")
					.setResultTransformer(Transformers.aliasToBean(Notification.class))
					.setLong("id", lastId)
					.setLong("account_id", accountId);
			
			cr = cr.setMaxResults(30);
			result = cr.list();
			session.getTransaction().commit();
		}
		catch(HibernateException ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return result;
	}

	@Override
	public Long getNotifyHistoryCount(Account account) {

		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			
			if (account != null) {
				Query cr = session.getNamedQuery("getNotificationHistoriesCount")
						.setParameter("account_id", account.getId());
				
				result = (Long) cr.uniqueResult();
			}
			if (result == null) result = 0L;
			session.getTransaction().commit();
		}
		catch(HibernateException ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return result;
	}

	@Override
	public Long getNotifyMessageCount(Account account) {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			
			Query cr = session.getNamedQuery("getNotificationMessagesCount")
					.setParameter("account_id", account.getId());
			
			result = (Long) cr.uniqueResult();
			if (result == null) result = 0L;
			session.getTransaction().commit();
		}
		catch(HibernateException ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Notification> getHistoryByAccountId(Long accountId, int page) {

		Collection<Notification> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			
			session.getNamedQuery("updateNotificationHistories")
				.setParameter("account_id", accountId)
				.setParameter("now", new Date())
				.executeUpdate();
				
			Query cr = session.getNamedQuery("getNotificationHistories")
					.setResultTransformer(Transformers.aliasToBean(Notification.class))
					.setParameter("account_id", accountId);

			cr = cr.setMaxResults(30).setFirstResult((page - 1) * 30);
			result = cr.list();
			session.getTransaction().commit();
		}
		catch(HibernateException ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return result;
	}

}
