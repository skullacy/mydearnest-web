package com.osquare.mydearnest.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

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
@Table(name = "PHOTO_TAG")
public class PhotoTag implements Serializable {

	private long id;
	private Account account;
	private Post post;
	
	private PostTag postTag;
	private String title;
	private String info;
	
	private long posX1;
	private long posY1;
	
	private long posX2;
	private long posY2;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSTTAG_ID", nullable = false)
	public PostTag getPostTag() {
		return postTag;
	}

	public void setPostTag(PostTag postTag) {
		this.postTag = postTag;
	}
	
	@Column(name = "TITLE", nullable = true)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "INFO", nullable = true)
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	@Column(name = "POSX1", nullable = false)
	public long getPosX1() {
		return posX1;
	}

	public void setPosX1(long posX1) {
		this.posX1 = posX1;
	}

	@Column(name = "POSY1", nullable = false)
	public long getPosY1() {
		return posY1;
	}

	public void setPosY1(long posY1) {
		this.posY1 = posY1;
	}
	
	@Column(name = "POSX2", nullable = false)
	public long getPosX2() {
		return posX2;
	}

	public void setPosX2(long posX2) {
		this.posX2 = posX2;
	}

	@Column(name = "POSY2", nullable = false)
	public long getPosY2() {
		return posY2;
	}

	public void setPosY2(long posY2) {
		this.posY2 = posY2;
	}

	

}
