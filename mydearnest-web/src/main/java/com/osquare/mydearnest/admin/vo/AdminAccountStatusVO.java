package com.osquare.mydearnest.admin.vo;

import java.io.Serializable;

import com.osquare.mydearnest.entity.Account;

public class AdminAccountStatusVO implements Serializable {
	
	private Account account;
	private long detailCount;
	private long gradeCount;
	private long postCount;
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public long getDetailCount() {
		return detailCount;
	}
	public void setDetailCount(long detailCount) {
		this.detailCount = detailCount;
	}
	
	public long getGradeCount() {
		return gradeCount;
	}
	public void setGradeCount(long gradeCount) {
		this.gradeCount = gradeCount;
	}
	
	public long getPostCount() {
		return postCount;
	}
	public void setPostCount(long postCount) {
		this.postCount = postCount;
	}
	
}
