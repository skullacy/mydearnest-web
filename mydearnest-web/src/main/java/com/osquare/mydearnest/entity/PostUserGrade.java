package com.osquare.mydearnest.entity;

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
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "POST_USER_GRADE")
public class PostUserGrade implements Serializable {

	private long id;
	private Account account;
	private Post post;
	private Date createdAt;
	
	private boolean feelModern;
	private boolean feelWarm;
	private boolean feelCute;
	private boolean feelLuxury;
	private boolean feelVintage;

	@Id
	@GeneratedValue(generator="UUIDPKGenerator")
	@GenericGenerator(name="UUIDPKGenerator", strategy="com.junglebird.webframe.common.PkGenerator")
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

	@Column(name = "CREATED_AT", nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "FEEL_MODERN", nullable = false)
	public boolean getFeelModern() {
		return feelModern;
	}

	public void setFeelModern(boolean feelModern) {
		this.feelModern = feelModern;
	}

	@Column(name = "FEEL_WARM", nullable = false)
	public boolean getFeelWarm() {
		return feelWarm;
	}

	public void setFeelWarm(boolean feelWarm) {
		this.feelWarm = feelWarm;
	}

	@Column(name = "FEEL_CUTE", nullable = false)
	public boolean getFeelCute() {
		return feelCute;
	}

	public void setFeelCute(boolean feelCute) {
		this.feelCute = feelCute;
	}

	@Column(name = "FEEL_LUXURY", nullable = false)
	public boolean getFeelLuxury() {
		return feelLuxury;
	}

	public void setFeelLuxury(boolean feelLuxury) {
		this.feelLuxury = feelLuxury;
	}

	@Column(name = "FEEL_VINTAGE", nullable = false)
	public boolean getFeelVintage() {
		return feelVintage;
	}

	public void setFeelVintage(boolean feelVintage) {
		this.feelVintage = feelVintage;
	}

}
