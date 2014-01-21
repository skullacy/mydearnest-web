package com.osquare.mydearnest.admin.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;












import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.RootEntityResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import com.osquare.mydearnest.admin.vo.AdminAccountStatusVO;
import com.osquare.mydearnest.entity.Account;

@Service("adminAccountService")
public class AdminAccountServiceImpl implements AdminAccountService {

	@Resource private SessionFactory sessionFactory;

	@Override
	@SuppressWarnings("unchecked")
	public Collection<AdminAccountStatusVO> findAccount(int page) {
		Collection<AdminAccountStatusVO> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			List<AdminAccountStatusVO> queryResult = session.createQuery("SELECT account as account"
					+ ", (SELECT COUNT(DISTINCT postTag.post) FROM PostTag postTag WHERE postTag.account.id = account.id) AS detailCount"
					+ ", (SELECT COUNT(DISTINCT postGrade.post) FROM PostGrade postGrade WHERE postGrade.account.id = account.id) AS gradeCount"
					+ ", (SELECT COUNT(*) FROM Post post WHERE post.account.id = account.id) AS postCount"
					+ " FROM Account account").setResultTransformer(Transformers.aliasToBean(AdminAccountStatusVO.class)).list();
			
//			Criteria cr = session.createCriteria(Account.class)
//					.addOrder(Order.desc("createdAt"))
//					.setMaxResults(10).setFirstResult((page - 1) * 10);
//			result = cr.list();
			
//			setResultTransformer(Transformers.aliasToBean(AdminAccountStatusVO.class))
//			, (SELECT count(DISTINCT postTag.post) from PostTag as postTag where postTag.account.id=account.id)
			
			result = queryResult;
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Long sizeOfAccount() {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Account.class)
					.setProjection(Projections.rowCount());
			result = (Long) cr.uniqueResult();
			
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Account updateRole(String userId, String role) {
		Account result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Account.class)
					.add(Restrictions.eq("id", Long.valueOf(userId)))
					.setMaxResults(1);
			
			result = (Account) cr.uniqueResult();

			if (result != null) {
				result.setRole(role);
				session.merge(result);
				
				//권한이 MODIFIER일 경우 index 추가하기.
				if("ROLE_MODIFIER".equals(role)) {
					cr = session.createCriteria(Account.class)
							.add(Restrictions.eq("role", "ROLE_MODIFIER"))
							.setProjection(Projections.rowCount());
					
					result.setModifierIndex((Long) cr.uniqueResult());
					
					session.update(result);
				}
				
			}
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}
	
}
