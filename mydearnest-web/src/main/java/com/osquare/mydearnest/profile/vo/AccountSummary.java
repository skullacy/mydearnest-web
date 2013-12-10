package com.osquare.mydearnest.profile.vo;

import com.osquare.mydearnest.entity.Account;


public class AccountSummary {

	private Account account;
	
	private Long commentCount;
	private Long findCount;

	private boolean isDuplicate;
	private Long isFollow;
	private Long followingCount;
	private Long followerCount;

	private boolean followToYou;
	private boolean followForMe;

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return this.account;
	}
	
	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getFindCount() {
		return findCount;
	}

	public void setFindCount(Long findCount) {
		this.findCount = findCount;
	}

	public void setIsFollow(Long isFollow) {
		this.isFollow = isFollow;
	}

	public Long getIsFollow() {
		return this.isFollow;
	}

	public void setFollowingCount(Long followingCount) {
		this.followingCount = followingCount;
	}

	public Long getFollowingCount() {
		return this.followingCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	public Long getFollowerCount() {
		return this.followerCount;
	}

	public void setIsDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public boolean getIsDuplicate() {
		return this.isDuplicate;
	}

	public void setFollowToYou(Long followToYou) {
		this.followToYou = (followToYou > 0);
	}

	public boolean getFollowToYou() {
		return this.followToYou;
	}

	public void setFollowForMe(Long followForMe) {
		this.followForMe = (followForMe > 0);
	}

	public boolean getFollowForMe() {
		return this.followForMe;
	}

}
