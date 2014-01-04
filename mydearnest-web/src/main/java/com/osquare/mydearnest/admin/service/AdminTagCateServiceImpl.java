package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.junglebird.webframe.common.StringUtils;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.vo.TagCategoryVO;

@Service("adminTagCateService")
public class AdminTagCateServiceImpl implements AdminTagCateService {
	
	@Resource private SessionFactory sessionFactory;

	@Override
	public Long sizeOfTag() {
		return null;
	}

	@Override
	public Collection<TagCategory> findTag(Integer page, String order) {
		return null;
	}

	@Override
	public TagCategory disabledTag(long id) {
		return null;
	}

	@Override
	public TagCategory getTagInfo(long id) {
		return null;
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
			
//			System.out.println(StringUtils.replaceEngPos(tagCateVO.getTitle()));
			
			String keyword = StringUtils.replaceEngPos(tagCateVO.getTitle());
			System.out.println(keyword);
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
	public Collection<TagCategory> getMatchedTagCategories(String keyword) {
		
		String keywordReplaced = StringUtils.replaceEngPos(keyword);
		
		Collection<TagCategory> result = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(TagCategory.class)
									.add(Restrictions.like("keyword", keywordReplaced, MatchMode.ANYWHERE));
			
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
