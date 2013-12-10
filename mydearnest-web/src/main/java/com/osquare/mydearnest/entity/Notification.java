package com.osquare.mydearnest.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "NOTIFICATION")
public class Notification implements Serializable {

	private long id;
	private Account account;
	private Post post;
	private String postMode;
	private Account postWriter;
	private Account follower;
	private Date createdAt;
	private Date confirmedAt;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_ID", nullable = false)
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	@Column(name = "POST_MODE", nullable = false)
	public String getPostMode() {
		return postMode;
	}

	public void setPostMode(String postMode) {
		this.postMode = postMode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_WRITE_ID", nullable = false)
	public Account getPostWriter() {
		return postWriter;
	}

	public void setPostWriter(Account postWriter) {
		this.postWriter = postWriter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOLLOW_ID", nullable = false)
	public Account getFollower() {
		return follower;
	}

	public void setFollower(Account follower) {
		this.follower = follower;
	}

	@Column(name = "CREATED_AT", nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "CONFIRMED_AT", nullable = false)
	public Date getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(Date confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

}
