package com.junglebird.webframe.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.osquare.mydearnest.entity.Account;


@SuppressWarnings("serial")
public class SignedDetails extends User {
	
	private Account account;
	public Account getAccount() {
		return account;
	}
	public Long getAccountId(){
		return account.getId();
	}

	public SignedDetails(String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.account = new Account();
	}
	
	public SignedDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.account = new Account();
	}

	public SignedDetails(Collection<? extends GrantedAuthority> authorities, Account account) {
		super(account.getEmail(), account.getPassword(), authorities);
		this.account = account;
	}
	
	public SignedDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, Account account) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.account = account;
	}

}