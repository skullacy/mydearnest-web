package com.osquare.mydearnest.admin.service;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;








import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;

@Service("adminPostService")
public class AdminPostServiceImpl implements AdminPostService {
	
	@Resource private SessionFactory sessionFactory;
	@Autowired private AccountService accountService;

	@Override
	public Long sizeOfPost() {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Post.class)
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
	@SuppressWarnings("unchecked")
	public Collection<Post> findPost(Integer page, String order, Integer checksum, Account account) {
		//트랜잭션 관계로 미리 빼놓기
		long modifierIndex = 0;
		if("ROLE_MODIFIER".equals(account.getRole())) {
			 modifierIndex = accountService.getTotalModifierCount();
		}
		
		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		Criteria cr = null;
		try {
			cr = session.createCriteria(Post.class)
					.setFetchMode("account", FetchMode.JOIN)
					.addOrder(Order.desc(order))
					.addOrder(Order.desc("createdAt"))
					.add(Restrictions.isNull("deletedOn"));
					
			if(checksum == 1) {
				cr.add(Restrictions.eq("checkSum", checksum == 1 ? true : false));
			}
			
			if("ROLE_MODIFIER".equals(account.getRole())) {
				cr.setMaxResults(10).setFirstResult((int) ((account.getModifierIndex() - 1 + (page - 1) * modifierIndex) * 10));
			}
			else {
				cr.setMaxResults(10).setFirstResult((page - 1) * 10);
			}
			
			result = cr.list();
			
			Iterator<Post> itr = result.iterator();
			while(itr.hasNext()) {
				Post tmpPost = null;
				tmpPost = itr.next();
				if(tmpPost.getGradeCount() > 0) Hibernate.initialize(tmpPost.getPostGrade());
				if(tmpPost.getPostTagCount() > 0) Hibernate.initialize(tmpPost.getPostTag());
				if(tmpPost.getPhotoTagCount() > 0) Hibernate.initialize(tmpPost.getPhotoTags());
			}
			
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Post disabledPost(long id) {
		Post result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Post.class)
					.add(Restrictions.eq("id", id))
					.setMaxResults(1);
			
			result = (Post) cr.uniqueResult();

			if (result != null) {
				result.setDeletedOn(new Date());
				session.merge(result);
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
