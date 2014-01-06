package com.osquare.mydearnest.post.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
















import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Category;
import com.osquare.mydearnest.entity.Folder;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Notification;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostComment;
import com.osquare.mydearnest.entity.PostGrade;
import com.osquare.mydearnest.entity.PostLove;
import com.osquare.mydearnest.entity.PostRank;
import com.osquare.mydearnest.entity.PostTag;
import com.osquare.mydearnest.post.vo.PostVO;
import com.osquare.mydearnest.post.vo.RefPost;
import com.osquare.mydearnest.profile.vo.CommentVO;

@Service("postService")
public class PostServiceImpl implements PostService {
	
	private final int GIVEPOINT_FOR_VIEW = 1;
	private final int GIVEPOINT_FOR_LOVE = 100;

	@Resource private SessionFactory sessionFactory;

	@Override
	public Collection<Folder> getUserFolders(Account account) {
		return this.getUserFolders(account, -1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Folder> getUserFolders(Account account, long folder_id) {

		Collection<Folder> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Folder.class)
							.add(Restrictions.eq("account", account))
							.addOrder(Order.desc("createdAt"));
			
			if (folder_id > 0) 
				cr = cr.add(Restrictions.ne("id", folder_id))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			result = cr.list();
			
			System.out.println(result.size());
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Folder getFolder(Long folderId) {

		Folder result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			result = (Folder) session.get(Folder.class, folderId);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Folder createFolder(String folderName, String desc, Account account) {
		
		Folder folder = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			folder = new Folder();
			folder.setAccount(account);
			folder.setName(folderName);
			folder.setDescription(desc);
			folder.setCreatedAt(new Date());
			session.persist(folder);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return folder;
	}
	
//	@Override
//	public Post createPost(Account account, ImageSource imageSource, PostVO postVO) {
//
//		Post post = null;
//		Session session = sessionFactory.getCurrentSession();
//		session.getTransaction().begin();
//
//		try {
////			Category category = new Category();
////			category.setId(postVO.getCategory());
//			
//			post = new Post();
//			post.setAccount(account);
//			post.setImageSource(imageSource);
//			
//			post.setImageWidth(imageSource.getWidth());
//			post.setImageHeight(imageSource.getHeight());
//			
////			post.setCategory(category);
//
//			post.setTitle(postVO.getTitle());
//			post.setDescription(postVO.getDesc());
//
//			post.setCreatedAt(new Date());
//			
//			post.setPosition(postVO.getPosition());
//			post.setHomeSize(postVO.getHomeSize());
//			post.setAreaType(postVO.getAreaType());
//			post.setAccessory(postVO.getAccessory());
//			
//			
//			
//			session.persist(post);
//			
//			
//			session.getTransaction().commit();
//			
//			this.createPostGrade(post, account, postVO);
//		}
//		catch(Exception ex) {
//			session.getTransaction().rollback();
//			ex.printStackTrace();
//		}
//		
//		return post;
//	}
	@Override
	public Post checkPostPublishable(Post post) {
		Post result = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			
			Criteria cr = null;
			cr = session.createCriteria(Account.class)
					.add(Restrictions.eq("role", "ROLE_ADMIN"))
					.setProjection(Projections.rowCount());
			
			Long maxGrader = (Long) cr.uniqueResult();
			
			
			cr = session.createCriteria(PostTag.class)
					.add(Restrictions.eq("post", post))
					.setProjection(Projections.rowCount());
			
			Long postTagCount = (Long) cr.uniqueResult();
			
			System.out.println(maxGrader);
			System.out.println(post.getGradeCount() == maxGrader);
			System.out.println(post.getSpaceType() >= 0);
			System.out.println(post.getTagSize() >= 0);
			System.out.println(post.getTagTone() >= 0);
			System.out.println(post.getTheme() >= 0);
			System.out.println(postTagCount >= 4);
			
			Boolean checkSum = (
						post.getGradeCount() == maxGrader &&
						post.getSpaceType() >= 0 &&
						post.getTagSize() >= 0 &&
						post.getTagTone() >= 0 &&
						post.getTheme() >= 0 &&
						postTagCount >= 4
					);
			
			cr = session.createCriteria(Post.class)
					.add(Restrictions.eq("id", post.getId()));
			
			result = (Post) cr.uniqueResult();
			result.setCheckSum(checkSum);
			
			session.update(result);
					
			session.getTransaction().commit();
		} catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return post;
		
	}
	
	@Override
	public Post createPostUpload(Account account, ImageSource imageSource,
			PostVO postVO) {
		Post post = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			post = new Post();
			post.setAccount(account);
			post.setImageSource(imageSource);
			
			post.setImageWidth(imageSource.getWidth());
			post.setImageHeight(imageSource.getHeight());
			
			post.setSource(postVO.getSource());
			
			post.setCreatedAt(new Date());
			
			session.persist(post);
			
			session.getTransaction().commit();
		} catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		checkPostPublishable(post);
		
		return post;
	}

	@Override
	public Post createPostDetail(Post post, Account account, PostVO postVO) {
		
		Post result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		Collection<PostTag> dResult = null;
		
		try {
			
				
			Criteria cr = session.createCriteria(PostTag.class)
					.add(Restrictions.eq("post", post));
			
			dResult = cr.list();
			for(PostTag postTag : dResult) {
				session.delete(postTag);
			}
			
			PostTag postTag = null;
			
			for(long tagId : postVO.getTagAccessory()) {
				postTag = null;
				postTag = new PostTag();
				postTag.setPost(post);
				postTag.setAccount(account);
				postTag.setTagCateId(tagId);
				
				session.persist(postTag);
			}
			
			for(long tagId : postVO.getTagHome()) {
				postTag = null;
				postTag = new PostTag();
				postTag.setPost(post);
				postTag.setAccount(account);
				postTag.setTagCateId(tagId);
				
				session.persist(postTag);
			}
			
			for(String value : postVO.getTagColor()) {
				postTag = null;
				postTag = new PostTag();
				postTag.setPost(post);
				postTag.setAccount(account);
				postTag.setNonCateType("color");
				postTag.setValue(value);
				
				session.persist(postTag);
			}
			
			result = post;
			result.setTagSize(postVO.getTagSize());
			result.setTagTone(postVO.getTagTone());
			result.setTheme(postVO.getTheme());
			result.setSpaceType(postVO.getSpaceType());
			
			session.update(result);
			
			
			
			session.getTransaction().commit();
			
			
		} catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		checkPostPublishable(post);
		
		return result;
	}
	
	@Override 
	public Post removePostByMode(Long postId, String editMode, Long drawerId) {
		Post result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {

			Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
			SignedDetails principal = (SignedDetails) authentication.getPrincipal();
			
			result = (Post) session.get(Post.class, postId);
//			if (editMode.equals("love")) {
//					
//				PostLove postLove = (PostLove) session.createCriteria(PostLove.class)
//					.add(Restrictions.eq("post", result))
//					.add(Restrictions.eq("account", session.get(Account.class, principal.getAccountId())))
//					.setMaxResults(1).uniqueResult();
//				
//				session.delete(postLove);
//			}
//			else if (editMode.equals("folder")) {
//				
//				Folder folder = (Folder) session.get(Folder.class, drawerId);
//				folder.getPosts().remove(result);
//				folder.setCount(folder.getPosts().size());
//				session.merge(folder);
//				
//				if (result.getAccount().getId() == principal.getAccountId()) session.delete(result);
//			}
//			else if (editMode.equals("post")) {
//				
//				for(Folder folder : result.getFolders()) {
//					folder.setCount(folder.getPosts().size());
//					session.merge(folder);
//				}
//
//				if (result.getAccount().getId() == principal.getAccountId()) session.delete(result);
//				
//			}
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Post getPostById(Long postId) {
		Post result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			Criteria cr = session.createCriteria(Post.class)
					.setFetchMode("category", FetchMode.JOIN)
					.add(Restrictions.isNull("deletedOn"))
					.add(Restrictions.eq("id", postId))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			result = (Post) cr.uniqueResult();
			
			Hibernate.initialize(result.getPostTag());
			
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
	public Collection<Post> getPostsOrderByLatest(Integer page, Long category) {

		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Post.class)
					.add(Restrictions.isNull("deletedOn"))
					.addOrder(Order.desc("createdAt"))
					.setMaxResults(30).setFirstResult((page - 1) * 30);
			
			if (category != null) {
				cr = cr.add(Restrictions.eq("category", session.get(Category.class, category)));
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
	@SuppressWarnings("unchecked")
	public Collection<Post> getPostsOrderByRank(Integer page) {
		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR, -48);
			
			StringBuilder sb = new StringBuilder();
			sb.append("select ");
			sb.append("post ");
			sb.append("from Post post ");
			sb.append("left outer join post.postRanks rank ");
			sb.append("where (rank.createdAt > :createdAt or rank is null) ");
			sb.append("and post.deletedOn is null ");
			sb.append("group by post ");
			sb.append("order by sum(rank.givePoint) desc, post.createdAt desc");
			
			Query q = session.createQuery(sb.toString())
					.setDate("createdAt", cal.getTime())
					.setMaxResults(30)
					.setFirstResult((page - 1) * 30);
			
			result = q.list();
			
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
	public Collection<Post> getPostsByFolder(Folder folder, Integer page) {
		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			DetachedCriteria detach = DetachedCriteria.forClass(Folder.class)
					.createAlias("posts", "post")
					.add(Restrictions.eq("id", folder.getId()))
					.setProjection(Projections.property("post.id"));
			
			Criteria cr = session.createCriteria(Post.class)
					.add(Restrictions.isNull("deletedOn"))
					.add(Property.forName("id").in(detach))
					.addOrder(Order.desc("createdAt"))
					.setMaxResults(30).setFirstResult((page - 1) * 30);;
			
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
	public Collection<Post> getPostsByKeyword(String keyword, Integer page) {

		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Query q = session.getNamedQuery("getPostsByKeyword")
					.setString("keyword", "%" + keyword + "%")
					.setParameterList("keywords", keyword.split(" "))
					.setMaxResults(30).setFirstResult((page - 1) * 30);
			
			result = q.list();
			
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
	public Collection<Post> getPostsByAccount(Account account) {

		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Post.class)
						.add(Restrictions.isNull("deletedOn"))
						.add(Restrictions.eq("account", account))
						.addOrder(Order.desc("createdAt"))
						.setMaxResults(9);
			
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
	public Collection<Post> getPostsByAccount(Account account, Integer page) {

		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Post.class)
						.add(Restrictions.isNull("deletedOn"))
						.add(Restrictions.eq("account", account))
						.addOrder(Order.desc("createdAt"))
						.setMaxResults(30).setFirstResult((page - 1) * 30);
			
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
	public Collection<Post> getPostsByLove(Account account, Integer page) {

		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			DetachedCriteria detach = DetachedCriteria.forClass(PostLove.class)
					.add(Restrictions.eq("account", account))
					.setProjection(Projections.property("post.id"));
			
			Criteria cr = session.createCriteria(Post.class)
						.add(Restrictions.isNull("deletedOn"))
						.add(Property.forName("id").in(detach))
						.addOrder(Order.desc("createdAt"))
						.setMaxResults(30).setFirstResult((page - 1) * 30);
			
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
	public Collection<PostGrade> getPostGradeByPost(Post post) {
		Collection<PostGrade> postGrade = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(PostGrade.class)
								.add(Restrictions.eq("post", post));
			
			postGrade = cr.list();
		}
		catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return postGrade;
	}

	@Override
	public PostGrade getMyPostGradeByPost(Account account, Post post) {
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria cr = session.createCriteria(PostGrade.class)
								.setFetchMode("account", FetchMode.JOIN)
								.add(Restrictions.eq("post", post))
								.add(Restrictions.eq("account", account))
								.setMaxResults(1);
			
			PostGrade postGrade = (PostGrade) cr.uniqueResult();
			
			session.getTransaction().commit();
			return postGrade;
		}
		catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<PostComment> getCommentsByPost(Post post, Integer page) {

		Collection<PostComment> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(PostComment.class)
					.setFetchMode("account", FetchMode.JOIN)
					.add(Restrictions.eq("post", post))
					.addOrder(Order.desc("id"))
					.setMaxResults(10).setFirstResult((page - 1) * 10);
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
	public PostComment createPostComment(Post post1, Account account, String text) {

		PostComment result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			Post post = (Post) session.get(Post.class, post1.getId());
			post.setCommentCount(post.getCommentCount() + 1);
			session.merge(post);
			
			result = new PostComment();
			result.setAccount(account);
			result.setPost(post);
			result.setText(text);
			result.setCreatedAt(new Date());
			
			session.persist(result);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public PostLove createPostLove(Account account, Long postId) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Post post = (Post) session.get(Post.class, postId);
			
			PostRank rank = new PostRank();
			rank.setAccount(account);
			rank.setPost(post);
			rank.setGivePoint(GIVEPOINT_FOR_LOVE);
			rank.setCreatedAt(new Date());
			session.persist(rank);
			
			PostLove object = new PostLove();
			object.setAccount(account);
			object.setPost(post);
			object.setCreatedAt(new Date());
			session.persist(object);

			Notification noti = new Notification();
			noti.setAccount(account);
			noti.setPost(post);
			noti.setPostWriter(post.getAccount());
			noti.setPostMode("LOVE");
			noti.setCreatedAt(new Date());
			session.persist(noti);
			
			Criteria cr = session.createCriteria(PostLove.class)
					.add(Restrictions.eq("post", post))
					.setProjection(Projections.rowCount())
					.setMaxResults(1);
			post.setGoodCount((Long) cr.uniqueResult());
			session.merge(post);
			
			session.getTransaction().commit();
			return object;
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public PostLove findPostLoveByAccount(Account account, Post post) {
		PostLove object = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(PostLove.class)
					.add(Restrictions.eq("account", account))
					.add(Restrictions.eq("post", post))
					.setMaxResults(1);
					
			object = (PostLove) cr.uniqueResult();
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		return object;
	}

	@Override
	public void deletePostLove(PostLove postLove) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {

			Post post = (Post) session.get(Post.class, postLove.getPost().getId());
			
			Criteria cr = session.createCriteria(Notification.class)
					.add(Restrictions.eq("post", post))
					.add(Restrictions.eq("postMode", "LOVE"))
					.add(Restrictions.eq("account", postLove.getAccount()))
					.setMaxResults(1);
			Notification noti = (Notification) cr.uniqueResult();
			if (noti != null) session.delete(noti);
			
			session.delete(postLove);
			

			cr = session.createCriteria(PostLove.class)
					.add(Restrictions.eq("post", post))
					.setProjection(Projections.rowCount())
					.setMaxResults(1);
			post.setGoodCount((Long) cr.uniqueResult());
			session.merge(post);
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
	}
	
	
	@Override
	public PostGrade createPostGrade(Post post1, Account account, PostVO postVO) {

		PostGrade result = null;
		Post post = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			post = (Post) session.get(Post.class, post1.getId());
			post.setGradeCount(post.getGradeCount() + 1);
			session.merge(post);
			
			result = new PostGrade();
			result.setAccount(account);
			result.setPost(post);
			result.setFeelCute(postVO.getFeelCute());
			result.setFeelWarm(postVO.getFeelWarm());
			result.setFeelModern(postVO.getFeelModern());
			result.setFeelVintage(postVO.getFeelVintage());
			result.setFeelLuxury(postVO.getFeelLuxury());
			result.setCreatedAt(new Date());
			
			session.persist(result);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		checkPostPublishable(post);
		
		return result;
	}
	
	@Override
	public PostGrade updatePostGrade(Post post1, Account account, PostVO postVO) {
		
		PostGrade result = getMyPostGradeByPost(account, post1);
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		
		try {
			
			result.setFeelCute(postVO.getFeelCute());
			result.setFeelWarm(postVO.getFeelWarm());
			result.setFeelModern(postVO.getFeelModern());
			result.setFeelVintage(postVO.getFeelVintage());
			result.setFeelLuxury(postVO.getFeelLuxury());
			
			session.update(result);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		checkPostPublishable(post1);
		
		return result;
	}

	
	@Override
	public Long getPrevPostId(Long postId, String ref) {
		
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			if (ref.toLowerCase().equals("latest")) {
				
				Criteria cr = session.createCriteria(Post.class)
						.add(Restrictions.isNull("deletedOn"))
						.add(Restrictions.lt("id", postId))
						.addOrder(Order.desc("id"))
						.setProjection(Projections.id())
						.setMaxResults(1);
				result = (Long) cr.uniqueResult();
			}
			else {

				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, -48);
				
				StringBuilder sb = new StringBuilder();
				sb.append("select ");
				sb.append("sum(rank.givePoint) as givePoint, post.createdAt as createdAt ");
				sb.append("from Post post ");
				sb.append("left outer join post.postRanks rank ");
				sb.append("where (rank.createdAt > :createdAt or rank is null) ");
				sb.append("and post.deletedOn is null ");
				sb.append("and post.id = :post_id ");
				sb.append("group by post ");

				Query q = session.createQuery(sb.toString())
						.setDate("createdAt", cal.getTime())
						.setLong("post_id", postId)
						.setResultTransformer(Transformers.aliasToBean(RefPost.class))
						.setMaxResults(1)
						.setFirstResult(0);
				
				RefPost refPost = (RefPost) q.uniqueResult();
				System.out.println(postId);
				System.out.println(refPost.getGivePoint());
				System.out.println(refPost.getCreatedAt());
				
				sb = new StringBuilder();
				sb.append("select ");
				sb.append("post.id ");
				sb.append("from Post post ");
				sb.append("left outer join post.postRanks rank ");
				sb.append("where (rank.createdAt > :createdAt or rank is null) ");
				sb.append("and post.deletedOn is null ");
				sb.append("group by post ");
				sb.append("order by sum(rank.givePoint) desc, post.createdAt desc");
				
				q = session.createQuery(sb.toString())
						.setDate("createdAt", cal.getTime())
//						.setLong("postGivePoint", refPost.getGivePoint())
						.setDate("postCreatedAt", refPost.getCreatedAt())
//						.setLong("post_id", postId)
						.setMaxResults(1)
						.setFirstResult(0);
				
				result = ((Long) q.uniqueResult());
				System.out.println(result);
				
			}
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		if (result == null) result = 0L;
		return result;
	}

	@Override
	public Long getNextPostId(Long postId, String ref) {
		
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {

			if (ref.toLowerCase().equals("latest")) {
				Criteria cr = session.createCriteria(Post.class)
						.add(Restrictions.isNull("deletedOn"))
						.add(Restrictions.gt("id", postId))
						.addOrder(Order.asc("id"))
						.setProjection(Projections.id())
						.setMaxResults(1);
				result = (Long) cr.uniqueResult();
			}
			else {

				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, -48);
				
				StringBuilder sb = new StringBuilder();
				sb.append("select ");
				sb.append("sum(rank.givePoint) as givePoint, post.createdAt as createdAt ");
				sb.append("from Post post ");
				sb.append("left outer join post.postRanks rank ");
				sb.append("where (rank.createdAt > :createdAt or rank is null) ");
				sb.append("and post.deletedOn is null ");
				sb.append("and post.id = :post_id ");
				sb.append("group by post ");

				Query q = session.createQuery(sb.toString())
						.setDate("createdAt", cal.getTime())
						.setLong("post_id", postId)
						.setResultTransformer(Transformers.aliasToBean(RefPost.class))
						.setMaxResults(1)
						.setFirstResult(0);
				
				RefPost refPost = (RefPost) q.uniqueResult();
				
				sb = new StringBuilder();
				sb.append("select ");
				sb.append("post.id ");
				sb.append("from Post post ");
				sb.append("left outer join post.postRanks rank ");
				sb.append("where (rank.createdAt > :createdAt or rank is null) ");
				sb.append("and post.deletedOn is null ");
				sb.append("and rank.givePoint >= :postGivePoint ");
				sb.append("and post.createdAt >= :postCreatedAt ");			
				sb.append("group by post ");
				sb.append("order by sum(rank.givePoint) desc, post.createdAt desc");
				
				q = session.createQuery(sb.toString())
						.setDate("createdAt", cal.getTime())
						.setLong("postGivePoint", refPost.getGivePoint())
						.setDate("postCreatedAt", refPost.getCreatedAt())
						.setMaxResults(1)
						.setFirstResult(1);
				
				result = ((Long) q.uniqueResult());
				
			}
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		if (result == null) result = 0L;
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PostLove> getPostLoveUsers(Post post) {

		List<PostLove> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			
			Criteria cr = session.createCriteria(PostLove.class)
					.setFetchMode("account", FetchMode.JOIN)
					.add(Restrictions.eq("post", post))
					.addOrder(Order.desc("id"))
					.setMaxResults(5);
			
			result = (List<PostLove>) cr.list();
			
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
	public List<Account> getPostRefindUsers(Long postId, int size) {

		List<Account> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
		
			Query q = session.createQuery("select account from Post post left join post.account account where post.refindId=:id group by account order by max(post.createdAt) desc")
					.setLong("id", postId).setMaxResults(size);
			
			result = (List<Account>) q.list();
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public long getPostRefindCount(Long postId) {

		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			Criteria cr = session.createCriteria(Post.class)
					.add(Restrictions.isNull("deletedOn"))
					.setProjection(Projections.groupProperty("account"));
			
			result = (long) cr.list().size();
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void createPostView(Account account, Post post) {

		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {

			PostRank rank = new PostRank();
			rank.setAccount(account);
			rank.setPost(post);
			rank.setGivePoint(GIVEPOINT_FOR_VIEW);
			rank.setCreatedAt(new Date());
			session.persist(rank);
			
			session.createSQLQuery("delete from POST_VIEW where post_id=:post_id and account_id=:account_id")
				.setLong("post_id", post.getId())
				.setLong("account_id", account.getId())
				.executeUpdate();
				
			session.createSQLQuery("insert into POST_VIEW (CREATED_AT, POST_ID, ACCOUNT_ID) values (NOW(), :post_id, :account_id);")
				.setLong("post_id", post.getId())
				.setLong("account_id", account.getId())
				.executeUpdate();
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Post> getReadedPostsByFolder(long account_id, Integer page) {

		Collection<Post> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Query q = session.createQuery("select post from PostView postView left join postView.post post where postView.account.id=:id order by postView.createdAt desc")
					.setLong("id", account_id)
					.setMaxResults(30)
					.setFirstResult((page - 1) * 30);
			
			result = (Collection<Post>) q.list();
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void removeFolder(Long folder_id, Long accountId) throws Exception {

		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			session.createSQLQuery("update POST set ENABLED=0, DELETED_ON=NOW() where FOLDER_ID=:folder_id and ACCOUNT_ID=:account_id")
				.setLong("folder_id", folder_id)
				.setLong("account_id", accountId)
				.executeUpdate();
			
			session.createSQLQuery("delete from FOLDER where ID=:folder_id and ACCOUNT_ID=:account_id")
				.setLong("folder_id", folder_id)
				.setLong("account_id", accountId)
				.executeUpdate();
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
			throw ex;
		}
		
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CommentVO> findPostCommentByAccountId(Long accountId,Integer page) {

		List<CommentVO> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append("POST.ID id, ");
			sb.append("POST.IMAGE_ID imageId, ");
			sb.append("ACCOUNT.`USER_ID` as `userId`, ");
			sb.append("ACCOUNT.`NAME` as `name`, ");
			sb.append("POST.TITLE as title, ");
			sb.append("POST_COMMENT.TEXT as text, ");
			sb.append("POST_COMMENT.CREATED_AT as createdAt, ");
			sb.append("M.CN as count ");
			sb.append("FROM ( ");
			sb.append("SELECT  ");
			sb.append("POST_ID, MAX(ID) CR, COUNT(*) CN ");
			sb.append("FROM POST_COMMENT  ");
			sb.append("WHERE ACCOUNT_ID=:account_id ");
			sb.append("GROUP BY POST_ID ");
			sb.append(") M ");
			sb.append("LEFT JOIN POST_COMMENT ");
			sb.append("ON M.CR = POST_COMMENT.ID ");
			sb.append("LEFT JOIN POST ");
			sb.append("ON M.POST_ID = POST.ID ");
			sb.append("LEFT JOIN ACCOUNT ");
			sb.append("ON POST.ACCOUNT_ID = ACCOUNT.ID ");
			sb.append("WHERE POST.ID IS NOT NULL ");
			sb.append("AND POST.DELETED_ON IS NULL ");
			sb.append("ORDER BY POST_COMMENT.CREATED_AT DESC");
			
			Query q = session.createSQLQuery(sb.toString())
					.setLong("account_id", accountId)
					.setMaxResults(30)
					.setFirstResult((page - 1) * 30)
					.setResultTransformer(Transformers.aliasToBean(CommentVO.class));
			
			result = (List<CommentVO>) q.list();
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean deletePost(Long postId) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {

			Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
			SignedDetails details = (SignedDetails) authentication.getPrincipal();
				
			Post post = (Post) session.get(Post.class, postId);
			if (post.getAccount().getId() != details.getAccountId()) throw new Exception();
			post.setDeletedOn(new Date());
			session.merge(post);
			
			session.getTransaction().commit();
			return true;
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
			return false;
		}
	}

	



}
