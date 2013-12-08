package com.junglebird.webframe.vo;

import java.util.Collection;

import net.sf.json.JSONObject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


@SuppressWarnings("serial")
public class SignedDetails extends User implements UserDetails {
	
	private JSONObject userData;
	public JSONObject getUserData() {
		return userData;
	}
	public Long getAccountId(){
		return userData.getLong("user_pk");
	}

	public SignedDetails(String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userData = new JSONObject();
	}
	
	public SignedDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.userData = new JSONObject();
	}

	public SignedDetails(String username, String password,
			Collection<? extends GrantedAuthority> authorities, JSONObject userData) {
		super(username, password, authorities);
		this.userData = userData;
	}
	
	public SignedDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, JSONObject userData) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.userData = userData;
	}
	public Object getImageId() {
		// TODO Auto-generated method stub
		return null;
	}

}