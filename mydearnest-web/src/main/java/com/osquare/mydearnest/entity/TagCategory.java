package com.osquare.mydearnest.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TAG_CATEGORY")
public class TagCategory implements Serializable {

	private long id;
	
	private String title;
	private String type;
	private String keyword;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "TYPE", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "KEYWORD", nullable = false)
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	

}
