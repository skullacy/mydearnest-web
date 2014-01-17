package com.osquare.mydearnest.account.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.osquare.mydearnest.account.vo.AccountDetails;
import com.osquare.mydearnest.account.vo.JoinDefault;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.profile.vo.AccountSummary;


@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Resource private SessionFactory sessionFactory;
	@Autowired private ShaPasswordEncoder passwordEncoder;
	
	@Autowired private JavaMailSender mailSender;
	@Autowired private VelocityEngine velocityEngine;
	@Autowired private MessageSource messageSource;

	// ID(PK) 로 회원찾기
	public Account findAccountById(long id) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			Criteria criteria = session.createCriteria(Account.class)
					.add(Restrictions.eq("id", id))
					.add(Restrictions.isNull("deletedOn"))
					.setMaxResults(1);
			
			Account account = (Account) criteria.uniqueResult();

			session.getTransaction().commit();
			return account;
		} 
		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}

	
	// 로그인정보로 회원찾기
	public Account findAccountByUserName(String userId) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			Criteria criteria = session.createCriteria(Account.class)
					.add(Restrictions.eq("userId", userId))
					.add(Restrictions.isNull("deletedOn"))
					.setMaxResults(1);
			Account account = (Account) criteria.uniqueResult();

			session.getTransaction().commit();

			return account;
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}



	@Override
	public Account findAccountBySocial(String type, String name) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			
			
			Criteria criteria = session.createCriteria(Account.class)
					.add(Restrictions.eq(type + "Id", name))
					.add(Restrictions.isNull("deletedOn"))
					.setMaxResults(1);
			Account account = (Account) criteria.uniqueResult();

			session.getTransaction().commit();

			return account;
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}


	@Override
	public AccountSummary findAccountSummaryBySocial(String type, String name, long visitor_id) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Query q = session
					.getNamedQuery("findAccountInfoBy" + type)
					.setResultTransformer(Transformers.aliasToBean(AccountSummary.class))
					.setLong("accountId", visitor_id)
					.setString("userId", name);
	
			AccountSummary result = (AccountSummary) q.uniqueResult();
	
			session.getTransaction().commit();
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
		
	}

	// 사용자 기본정보 표출
	public AccountSummary findAccountSummaryById(long accountId, long visitor_id) {

		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Query q = session
					.getNamedQuery("findAccountInfo")
					.setResultTransformer(Transformers.aliasToBean(AccountSummary.class))
					.setLong("userId", accountId);
	
			AccountSummary result = (AccountSummary) q.uniqueResult();
	
			session.getTransaction().commit();
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}
	


	// 아이디 중복체크
	public boolean isNotFoundUser(String userId) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 3);
			
			Criteria criteria = session.createCriteria(Account.class)
									.add(Restrictions.eq("email", userId))
									.add(Restrictions.eq("enabled", true))
									.add(Restrictions.or(
											Restrictions.lt("deletedOn", cal.getTime()),
											Restrictions.isNull("deletedOn")
											))
									.setMaxResults(1);
			
			Account account = (Account) criteria.uniqueResult();
			session.getTransaction().commit();
			
			return (account == null);
		} 
		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	
	// 사용자 생성 (가입)
	public Account createAccount(JoinDefault accountVO) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			
			Criteria cr = session.createCriteria(Account.class)
					.add(Restrictions.eq("email", accountVO.getMailAddress()))
					.add(Restrictions.eq("enabled", false))
					.setMaxResults(1);
			
			Account account = (Account) cr.uniqueResult();
			if (account == null) {
				account = new Account();
							
				account.setEmail(accountVO.getMailAddress());
				account.setPassword(passwordEncoder.encodePassword(accountVO.getPassword(), null));
				
				account.setFacebookId(accountVO.getFacebookId());
				account.setSocialTokens(accountVO.getSocialData());
				account.setRole("ROLE_USER");
				
				account.setEnabled(false);
				account.setCreatedAt(new Date());
				
				//account.setId((Long) session.save(account));
				
				session.save(account);
			}
			else {
				account.setPassword(passwordEncoder.encodePassword(accountVO.getPassword(), null));
				account.setFacebookId(accountVO.getFacebookId());
				account.setSocialTokens(accountVO.getSocialData());
			}
			session.getTransaction().commit();
			
			return account;
		} 
		catch (HibernateException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		}
	}
	
	public Account enabledAccount(AccountDetails accountVO, ImageSource source) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Account account = (Account) session.get(Account.class, accountVO.getId());
						
			account.setName(accountVO.getUsername());
			account.setRegion(accountVO.getRegion());
			account.setBio(accountVO.getBio());
			
			if (source != null) account.setImageId(source.getId());
			
			account.setEnabled(true);
			account.setCreatedAt(new Date());
			
			session.merge(account);
			session.getTransaction().commit();
			
			return account;
		} 
		catch (HibernateException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return null;
	}

	// 사용자 로그인 후 정보 변경
	public Account updateSignInfo(Account account, String remoteAddr) {

		Account accountOriginal = this.findAccountByUserName(account.getEmail());

		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		try {
			accountOriginal.setLastSignedAt(new Date());
			accountOriginal.setLastSignedIp(remoteAddr);

			session.update(accountOriginal);
			session.getTransaction().commit();
			
			return account;
		}
		catch (HibernateException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
	}
	

	
	


	
	

	/***
	 * 회원 비밀번호 변경
	 * 
	 * @param 회원
	 *            데이터PK
	 * @param 비밀번호
	 */
	public void updateAccountPassword(long account_id, String password) {

		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		Account account = (Account) session.get(Account.class, account_id);
		try {
			account.setPassword(passwordEncoder.encodePassword(password, null));
			account.setUpdatedOn(new Date());
			session.merge(account);
			
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}



	/***
	 * 회원 기본정보 수정
	 * 
	 * @param 회원
	 *            데이터PK
	 * @param 사용자
	 *            기본정보가 담긴 VO
	 */

	public Account updateAccount(long id, AccountDetails accountVO, ImageSource imageSource) {

		Account account = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			account = (Account) session.get(Account.class, id);
			account.setName(accountVO.getUsername());
			account.setRegion(accountVO.getRegion());
			account.setBio(accountVO.getBio());
			
			if (imageSource != null) account.setImageId(imageSource.getId());
			
			account.setUpdatedOn(new Date());
			
			session.merge(account);
			session.getTransaction().commit();
		} catch (Exception ex) {
			account = null;
			ex.printStackTrace();
			session.getTransaction().rollback();
		}

		return account;
	}

	@Override
	public void updateSocialTokens(long id, String socialTokens) {
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try{
			Account account = (Account) session.get(Account.class, id);
			account.setSocialTokens(socialTokens);
			session.merge(account);
			
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
	}

	@Override
	public Account refreshAccountPassword(String mailAddress) {

		Account account = null;
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		try {
			Criteria cr = session.createCriteria(Account.class)
					.add(Restrictions.eq("email", mailAddress))
					.add(Restrictions.isNull("deletedOn"))
					.setMaxResults(1);
	
			account = (Account)cr.uniqueResult();
			

			if (account != null) {
				
				String password = "";
				for(int i=0; i<9; i++)
				{
					int random = 0 + (int)(Math.random() * 9);
					password += String.valueOf(random);
				}	
				
				account.setPassword(passwordEncoder.encodePassword(password, null));
				account.setUpdatedOn(new Date());
				session.merge(account);

				HashMap<String, Object> model = new HashMap<String, Object>();
				model.put("account", account);
				model.put("new_password", password);
				model.put("site_uri", "http://www.findfashion.kr");
	
				String htmlString = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail/help.vm", "UTF-8", model);
	
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = null;
				
				try {
					mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					mimeMessageHelper.setFrom("FindFashion <findfashion@naver.com>");
					mimeMessageHelper.setTo(account.getEmail());
					mimeMessageHelper.setSubject(messageSource.getMessage("lang.mail.help_title", null, Locale.getDefault()));
					mimeMessageHelper.setText(htmlString, true);
					
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				
				if (mimeMessageHelper != null) this.mailSender.send(mimeMessage);
			}
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return account;
	}


	@Override
	public void updateAccount(Account me) {
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try{
			session.merge(me);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		}
		
	}


	@Override
	public long getTotalModifierCount() {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			Criteria cr = session.createCriteria(Account.class)
					.add(Restrictions.eq("role", "ROLE_MODIFIER"))
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


}
