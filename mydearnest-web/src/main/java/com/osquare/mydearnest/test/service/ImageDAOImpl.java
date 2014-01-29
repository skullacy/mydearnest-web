package com.osquare.mydearnest.test.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;

@Repository("imageDAO")
public class ImageDAOImpl implements ImageDAO {

	@Resource private SessionFactory sessionFactory;
	
	public ImageDAOImpl() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageSource> getImageSourceList() {
		List<Post> postList = null; 
		List<ImageSource> imageSourceList = new ArrayList<ImageSource>();
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria criteria = session.createCriteria(Post.class).setFetchMode("imageSource", FetchMode.JOIN);
			
			postList = criteria.list();
			
			Iterator<Post> iterator = postList.iterator();
			
			while (iterator.hasNext()) {
				imageSourceList.add(iterator.next().getImageSource());
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return imageSourceList;
	}

	@Override
	public boolean updateAvgColor(ImageSource imageSource) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			session.update(imageSource);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return true;
	}

}
