package com.osquare.mydearnest.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "POST")
public class Post implements Serializable {

	private long id;
	private Category category;
	private Account account;
	
	private ImageSource imageSource;
	private int imageWidth;
	private int imageHeight;
	
	private String title;
	private String description;
	
	private long goodCount;
	private long commentCount;
	private long drawerCount;
	
	private Date createdAt;
	private Date deletedOn;
	
	private Set<Folder> folders = new HashSet<Folder>(0);
	
	private Set<Notification> notifications = new HashSet<Notification>(0);
	private Set<PostLove> postLoves = new HashSet<PostLove>(0);
	private Set<PostComment> postComments = new HashSet<PostComment>(0);
	
	private Set<PostView> postViews = new HashSet<PostView>(0);
	private Set<PostRank> postRanks = new HashSet<PostRank>(0);
	

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
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_ID", nullable = false)
	public ImageSource getImageSource() {
		return imageSource;
	}

	public void setImageSource(ImageSource imageSource) {
		this.imageSource = imageSource;
	}
	
	@Column(name = "IMAGE_WIDTH", nullable = false)
	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	@Column(name = "IMAGE_HEIGHT", nullable = false)
	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Column(name = "DESCRIPTION", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "GOOD_COUNT", nullable = false)
	public long getGoodCount() {
		return goodCount;
	}

	public void setGoodCount(long goodCount) {
		this.goodCount = goodCount;
	}

	@Column(name = "COMMENT_COUNT", nullable = false)
	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	@Column(name = "DRAWER_COUNT", nullable = false)
	public long getDrawerCount() {
		return drawerCount;
	}

	public void setDrawerCount(long drawerCount) {
		this.drawerCount = drawerCount;
	}

	@Column(name = "CREATED_AT", nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "DELETED_ON", nullable = true)
	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<PostLove> getPostLoves() {
		return postLoves;
	}

	public void setPostLoves(Set<PostLove> postLoves) {
		this.postLoves = postLoves;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<PostComment> getPostComments() {
		return postComments;
	}

	public void setPostComments(Set<PostComment> postComments) {
		this.postComments = postComments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<PostView> getPostViews() {
		return postViews;
	}

	public void setPostViews(Set<PostView> postViews) {
		this.postViews = postViews;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<PostRank> getPostRanks() {
		return postRanks;
	}

	public void setPostRanks(Set<PostRank> postRanks) {
		this.postRanks = postRanks;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "POST_FOLDER", joinColumns = { @JoinColumn(name = "POST_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "FOLDER_ID", nullable = false, updatable = false) })
	public Set<Folder> getFolders() {
		return folders;
	}

	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}

	
	
	
	

}
