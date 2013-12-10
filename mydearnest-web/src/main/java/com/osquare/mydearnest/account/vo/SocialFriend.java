package com.osquare.mydearnest.account.vo;

import com.osquare.mydearnest.profile.vo.AccountSummary;

public class SocialFriend {

	private String id;
	private String username;
	private String imageUri;
	private AccountSummary user;
	private String voteUri;
	private boolean newWindow;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public AccountSummary getUser() {
		return user;
	}

	public void setUser(AccountSummary user) {
		this.user = user;
	}

	public String getVoteUri() {
		return voteUri;
	}

	public void setVoteUri(String voteUri) {
		this.voteUri = voteUri;
	}

	public boolean isNewWindow() {
		return newWindow;
	}

	public void setNewWindow(boolean newWindow) {
		this.newWindow = newWindow;
	}

}
