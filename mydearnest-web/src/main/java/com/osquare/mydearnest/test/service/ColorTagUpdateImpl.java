package com.osquare.mydearnest.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.osquare.mydearnest.entity.PostTag;

@Service("colorTagUpdate")
public class ColorTagUpdateImpl implements ColorTagUpdate {

	@Resource private SessionFactory sessionFactory;
	
	@Override
	public List<PostTag> getWrongColorTags() {
		List<PostTag> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			Criteria criteria = session.createCriteria(PostTag.class).add(Restrictions.like("value", "%0")).addOrder(Order.asc("post.id"));
			
			result = criteria.list();
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return result;
	}

}
