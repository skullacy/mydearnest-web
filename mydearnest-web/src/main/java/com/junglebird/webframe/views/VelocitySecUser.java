package com.junglebird.webframe.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.junglebird.webframe.vo.SignedDetails;

public class VelocitySecUser {

	/**
	 * Gets the user name of the user from the Authentication object
	 * 
	 * @return the user name as string
	 */
	public static String getPrincipal() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (obj instanceof UserDetails) {
			return ((UserDetails) obj).getUsername();
		} else {
			return "Guest";
		}
	}
	
	public Long getAccountId() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (obj instanceof UserDetails) {
			return ((SignedDetails) obj).getAccountId();
		} else {
			return -1L;
		}
	}
	
	public static String getPrincipalName() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (obj instanceof UserDetails) {
			return ((SignedDetails) obj).getUserData().getString("user_name");
		} else {
			return "Guest";
		}
	}

	/**
	 * Is the user granted all of the grantedAuthorities passed in
	 * 
	 * @param roles
	 *            a string array of grantedAuth
	 * @return true if user has all of the listed authorities/roles, otherwise
	 *         false
	 */
	public static boolean allGranted(String[] checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		for (String auth : checkForAuths) {
			System.out.println(auth);
			if (userAuths.contains(auth))
				continue;
			return false;
		}
		return true;
	}

	/**
	 * Is the user granted any of the grantedAuthorities passed into
	 * 
	 * @param roles
	 *            a string array of grantedAuth
	 * @return true if user has any of the listed authorities/roles, otherwise
	 *         false
	 */
	public static boolean anyGranted(String[] checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		for (String auth : checkForAuths) {
			if (userAuths.contains(auth))
				return true;
		}
		return false;
	}

	/**
	 * is the user granted none of the supplied roles
	 * 
	 * @param roles
	 *            a string array of roles
	 * @return true only if none of listed roles are granted
	 */
	public static boolean noneGranted(String[] checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		for (String auth : checkForAuths) {
			if (userAuths.contains(auth))
				return false;
		}
		return true;
	}

	private static Set<String> getUserAuthorities() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<String> roles = new HashSet<String>();
		if (obj instanceof UserDetails) {
			Collection<? extends GrantedAuthority> gas = ((UserDetails) obj).getAuthorities();
			for (GrantedAuthority ga : gas) {
				System.out.println(ga.getAuthority());
				roles.add(ga.getAuthority());
			}
		}
		return roles;
	}
}