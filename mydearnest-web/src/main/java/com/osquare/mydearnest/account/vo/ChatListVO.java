package com.osquare.mydearnest.account.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ChatListVO {
	
	private long accountId;
	private String accountName;
	private long accountImage;
	private long receiverId;
	private String receiverName;
	private long receiverImage;
	private String content;
	private Date createdAt;
	
	public ChatListVO() {
		
	}
	
	public ChatListVO(long accountId, String accountName, long accountImage, long receiverId, String receiverName, long receiverImage, String content, Date createdAt) {
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountImage = accountImage;
		this.receiverId = receiverId;
		this.receiverName = receiverName;
		this.receiverImage = receiverImage;
		this.content = content;
		this.createdAt = createdAt;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public long getAccountImage() {
		return accountImage;
	}

	public void setAccountImage(BigDecimal accountImage) {
		this.accountImage = accountImage.longValue();
	}
		
	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public long getReceiverImage() {
		return receiverImage;
	}

	public void setReceiverImage(BigDecimal receiverImage) {
		this.receiverImage = receiverImage.longValue();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
