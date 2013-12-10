package com.osquare.mydearnest.account.vo;

import com.osquare.mydearnest.entity.Account;


public class MessageVO {
	private Account sendAccountid;
	private long receiveAccountid;
	private String content;

	public Account getSendAccountid() {
		return sendAccountid;
	}

	public long getReceiveAccountid() {
		return receiveAccountid;
	}

	public String getContent() {
		return content;
	}

	public void setSendAccountid(Account sendAccountid) {
		this.sendAccountid = sendAccountid;
	}

	public void setReceiveAccountid(long receiveAccountid) {
		this.receiveAccountid = receiveAccountid;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
