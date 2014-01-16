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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ACCOUNT")
public class Account implements Serializable {

	private long id;
	private String email;
	private String password;
	private String name;

	private boolean receiveMail;
	private String role;
	private long modifierIndex;

	private String region;

	private String bio;
	private Long imageId;
	
	private boolean enabled;

	private Date createdAt;
	private Date updatedOn;
	private Date deletedOn;

	private Date lastSignedAt;
	private String lastSignedIp;

	private String facebookId;
	private String socialTokens;

	private Set<Folder> folders = new HashSet<Folder>(0);
	private Set<Post> posts = new HashSet<Post>(0);

	private Set<Notification> notifications = new HashSet<Notification>(0);
	private Set<Notification> notificationsForFollower = new HashSet<Notification>(0);

	private Set<PostLove> postLoves = new HashSet<PostLove>(0);
	private Set<PostComment> postComments = new HashSet<PostComment>(0);
	private Set<PostView> postViews = new HashSet<PostView>(0);

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "EMAIL", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "ROLE", nullable = false)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Column(name = "MODIFIER_INDEX", nullable = false)
	public long getModifierIndex() {
		return modifierIndex;
	}

	public void setModifierIndex(long modifierIndex) {
		this.modifierIndex = modifierIndex;
	}

	@Column(name = "NAME", nullable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "RECEIVE_MAIL", nullable = false)
	public boolean isReceiveMail() {
		return receiveMail;
	}

	public void setReceiveMail(boolean receiveMail) {
		this.receiveMail = receiveMail;
	}

	@Column(name = "REGION", nullable = true)
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "BIO", nullable = true)
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	@Column(name = "IMAGE_ID", nullable = true)
	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	@Column(name = "ENABLED", nullable = false)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "CREATED_AT", nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "UPDATED_ON", nullable = true)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "DELETED_ON", nullable = true)
	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	@Column(name = "LAST_SIGNED_AT", nullable = true)
	public Date getLastSignedAt() {
		return lastSignedAt;
	}

	public void setLastSignedAt(Date lastSignedAt) {
		this.lastSignedAt = lastSignedAt;
	}

	@Column(name = "LAST_SIGNED_IP", nullable = true)
	public String getLastSignedIp() {
		return lastSignedIp;
	}

	public void setLastSignedIp(String lastSignedIp) {
		this.lastSignedIp = lastSignedIp;
	}

	@Column(name = "FACEBOOK_ID", nullable = true)
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Column(name = "SOCIAL_TOKENS", nullable = true)
	public String getSocialTokens() {
		return socialTokens;
	}

	public void setSocialTokens(String socialTokens) {
		this.socialTokens = socialTokens;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<Folder> getFolders() {
		return folders;
	}

	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "follower")
	public Set<Notification> getNotificationsForFollower() {
		return notificationsForFollower;
	}

	public void setNotificationsForFollower(
			Set<Notification> notificationsForFollower) {
		this.notificationsForFollower = notificationsForFollower;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<PostLove> getPostLoves() {
		return postLoves;
	}

	public void setPostLoves(Set<PostLove> postLoves) {
		this.postLoves = postLoves;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<PostComment> getPostComments() {
		return postComments;
	}

	public void setPostComments(Set<PostComment> postComments) {
		this.postComments = postComments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<PostView> getPostViews() {
		return postViews;
	}

	public void setPostViews(Set<PostView> postViews) {
		this.postViews = postViews;
	}

}
