package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.junglebird.webframe.common.StringUtils;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.vo.TagCategoryVO;

@Service("adminTagCateService")
public class AdminTagCateServiceImpl implements AdminTagCateService {
	
	@Resource private SessionFactory sessionFactory;

	@Override
	public Long sizeOfTag(String type) {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(TagCategory.class)
					.setProjection(Projections.rowCount());
			
			if(!type.isEmpty()) {
				cr.add(Restrictions.eq("type", type));
			}
			
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
	public Collection<TagCategory> findTag(Integer page, String type, String order) {
		Collection<TagCategory> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			Criteria cr = session.createCriteria(TagCategory.class)
//					.addOrder(Order.desc(order))
					.setMaxResults(10).setFirstResult((page - 1) * 20);
			
			if(!type.isEmpty()) {
				cr.add(Restrictions.eq("type", type));
			}
			
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
	public TagCategory disabledTag(long id) {
		return null;
	}

	@Override
	public TagCategory getTagInfo(long id) {
		TagCategory result = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(TagCategory.class)
					.add(Restrictions.eq("id", id));
			
			result = (TagCategory) cr.uniqueResult();
			
			session.getTransaction().commit();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public TagCategory createTagCategory(TagCategoryVO tagCateVO) {
		
		TagCategory tagCate = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			tagCate = new TagCategory();
			
			tagCate.setTitle(tagCateVO.getTitle());
			tagCate.setType(tagCateVO.getType());
			
			String keyword = StringUtils.replaceEngPos(tagCateVO.getTitle());
			tagCate.setKeyword(keyword);
			
			session.persist(tagCate);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		
		return tagCate;
	}
	
	@Override
	public TagCategory updateTagCategory(TagCategoryVO tagCateVO, long tagCateId) {
		
		TagCategory tagCate = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			tagCate = (TagCategory) session.get(TagCategory.class, tagCateId);
			
			tagCate.setTitle(tagCateVO.getTitle());
			tagCate.setType(tagCateVO.getType());
			
			String keyword = StringUtils.replaceEngPos(tagCateVO.getTitle());
			tagCate.setKeyword(keyword);
			
			session.update(tagCate);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		
		return tagCate;
	}
	
	@Override
	public Boolean deleteTagCategory(long tagCateId) {
		Boolean result = false;
		TagCategory tagCate;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			tagCate = (TagCategory) session.get(TagCategory.class, tagCateId);
			
			session.delete(tagCate);
			session.getTransaction().commit();
			result = true;
		}
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		
		return result;
	}

	@Override
	public Collection<TagCategory> getMatchedTagCategories(String keyword, String type) {
		
		String keywordReplaced = StringUtils.replaceEngPos(keyword);
		
		Collection<TagCategory> result = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		Criteria cr = null;
		try {
			if("all".equals(type)) {
				cr = session.createCriteria(TagCategory.class)
						.add(Restrictions.like("keyword", keywordReplaced, MatchMode.ANYWHERE));
			}
			else {
				cr = session.createCriteria(TagCategory.class)
						.add(Restrictions.eq("type", type))
						.add(Restrictions.like("keyword", keywordReplaced, MatchMode.ANYWHERE));
			}
			
			
			result = cr.list();
			
			session.getTransaction().commit();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Collection<TagCategory> getTagCategories() {
		Collection<TagCategory> result = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(TagCategory.class);
			
			result = cr.list();
			
			session.getTransaction().commit();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return result;
	}

}
