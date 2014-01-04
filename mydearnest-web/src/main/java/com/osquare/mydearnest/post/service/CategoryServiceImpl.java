package com.osquare.mydearnest.post.service;

import java.util.Collection;

import javax.annotation.Resource;






import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.junglebird.webframe.common.StringUtils;
import com.osquare.mydearnest.entity.Category;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Resource private SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Category> getRootCategories() {
		Collection<Category> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Category.class)
							.add(Restrictions.eq("parentId", 0L))
							.addOrder(Order.asc("orderIndex"));
			
			result = cr.list();
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
	public Collection<Category> getChildCategories(Long id) {

		Collection<Category> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Category.class)
							.add(Restrictions.eq("parentId", id))
							.addOrder(Order.asc("orderIndex"));
			
			result = cr.list();
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}


	/**
	 * @brief 카테고리 Autocomplete 리턴용 메서드
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Category> getMatchedCategories(String keyword) {
		
		System.out.println(keyword);
		String keywordReplaced = StringUtils.replaceEngPos(keyword);
		System.out.println(keywordReplaced);
		
		Collection<Category> result = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(Category.class)
								.add(Restrictions.like("searchTag", keywordReplaced, MatchMode.ANYWHERE));
			
			result = cr.list();
			session.getTransaction().commit();
			
		}
		catch (NullPointerException ex){
			ex.printStackTrace();
			return null;
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		return result;
	}

}
