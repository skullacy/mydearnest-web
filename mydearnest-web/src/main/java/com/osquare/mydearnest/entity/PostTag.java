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
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "POST_TAG")
public class PostTag implements Serializable {

	private long id;
	private Account account;
	private Post post;
	
	
	private String nonCateType;
	private long tagCateId;
	private String value;
	
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

	@Column(name = "VALUE", nullable = true)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "NONCATETYPE", nullable = true)
	public String getNonCateType() {
		return nonCateType;
	}
	
	public void setNonCateType(String nonCateType) {
		this.nonCateType = nonCateType;
	}
	
	@Column(name = "TAGCATEID", nullable = true)
	public long getTagCateId() {
		return tagCateId;
	}

	public void setTagCateId(long tagCateId) {
		this.tagCateId = tagCateId;
	}

	

	

}
