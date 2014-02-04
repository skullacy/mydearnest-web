package com.osquare.mydearnest.account.service;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.entity.Account;

@SuppressWarnings("deprecation")
public class MemberDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		Account account = null;
		
		try {
			
			Criteria criteria = session.createCriteria(Account.class)
									.add(Restrictions.eq("email", userId.toLowerCase()))
									.add(Restrictions.isNull("deletedOn"))
									.setMaxResults(1);

			account = (Account) criteria.uniqueResult();
			session.getTransaction().commit();
		}
		catch(HibernateException ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}


		if (account == null) throw new UsernameNotFoundException("user not found");		
		
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
		for(String role : account.getRole().split("\\|")) {
			authorities.add(new GrantedAuthorityImpl(role));
		}

		SignedDetails user = new SignedDetails(authorities, account);
		return user;
		
	}

}
