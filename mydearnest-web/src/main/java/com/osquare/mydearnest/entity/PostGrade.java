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
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "POST_GRADE")
public class PostGrade implements Serializable {

	private long id;
	private Account account;
	private Post post;
	private Date createdAt;
	
	private float feelModern;
	private float feelWarm;
	private float feelCute;
	private float feelLuxury;
	private float feelVintage;

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
	public float getFeelModern() {
		return feelModern;
	}

	public void setFeelModern(float feelModern) {
		this.feelModern = feelModern;
	}

	@Column(name = "FEEL_WARM", nullable = false)
	public float getFeelWarm() {
		return feelWarm;
	}

	public void setFeelWarm(float feelWarm) {
		this.feelWarm = feelWarm;
	}

	@Column(name = "FEEL_CUTE", nullable = false)
	public float getFeelCute() {
		return feelCute;
	}

	public void setFeelCute(float feelCute) {
		this.feelCute = feelCute;
	}

	@Column(name = "FEEL_LUXURY", nullable = false)
	public float getFeelLuxury() {
		return feelLuxury;
	}

	public void setFeelLuxury(float feelLuxury) {
		this.feelLuxury = feelLuxury;
	}

	@Column(name = "FEEL_VINTAGE", nullable = false)
	public float getFeelVintage() {
		return feelVintage;
	}

	public void setFeelVintage(float feelVintage) {
		this.feelVintage = feelVintage;
	}


}
