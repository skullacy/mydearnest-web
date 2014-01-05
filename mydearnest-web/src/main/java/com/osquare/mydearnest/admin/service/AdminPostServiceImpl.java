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
import org.springframework.stereotype.Service;

import com.osquare.mydearnest.entity.Post;

@Service("adminPostService")
public class AdminPostServiceImpl implements AdminPostService {
	
	@Resource private SessionFactory sessionFactory;

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
	public Collection<Post> findPost(Integer page, String order) {
		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Post.class)
					.setFetchMode("account", FetchMode.JOIN)
					.addOrder(Order.desc(order))
					.addOrder(Order.desc("createdAt"))
					.add(Restrictions.isNull("deletedOn"))
					.setMaxResults(10).setFirstResult((page - 1) * 20);
			result = cr.list();
			
			Iterator<Post> itr = result.iterator();
			while(itr.hasNext()) {
				Hibernate.initialize(itr.next().getPostGrade());
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
