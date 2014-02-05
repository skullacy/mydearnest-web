package com.osquare.mydearnest.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "POST")
public class Post implements Serializable {

	private long id;
	private Account account;
	
	private ImageSource imageSource;
	private int imageWidth;
	private int imageHeight;
	
	private String source;
	
	private Date createdAt;
	private Date deletedOn;
	private Boolean checkSum;
	
	

	
	//포스트 관련 수치들
	private long goodCount;
	private long commentCount;
	private long gradeCount;
	private long photoTagCount;
	private long postTagCount;
	private Set<PostLove> postLoves = new HashSet<PostLove>(0);
	private Set<PostComment> postComments = new HashSet<PostComment>(0);
	private Set<PostView> postViews = new HashSet<PostView>(0);
	
	//사진 평가모음
	private Set<PostGrade> postGrades = new HashSet<PostGrade>(0);
	
	//포스트 태그모음(중복입력 가능한것들)
	private Set<PostTag> postTags = new HashSet<PostTag>(0);
	
	//사진 태그모음
	private Set<PhotoTag> photoTags = new HashSet<PhotoTag>(0);
	
	//단일입력만 가능한 것들
	private long tagSize;
	private long tagTone;
	
	
	private int theme;
	private int spaceType;

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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	@Column(name = "SOURCE", columnDefinition="TEXT", nullable = false)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Column(name = "SIZE", nullable = true)
	public long getTagSize() {
		return tagSize;
	}

	public void setTagSize(long tagSize) {
		this.tagSize = tagSize;
	}

	@Column(name = "TONE", nullable = true)
	public long getTagTone() {
		return tagTone;
	}

	public void setTagTone(long tagTone) {
		this.tagTone = tagTone;
	}

	@Column(name = "THEME", nullable = true)
	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}

	@Column(name = "SPACETYPE", nullable = true)
	public int getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(int spaceType) {
		this.spaceType = spaceType;
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

	@Column(name = "GRADE_COUNT", nullable = false)
	public long getGradeCount() {
		return gradeCount;
	}

	public void setGradeCount(long gradeCount) {
		this.gradeCount = gradeCount;
	}
	
	@Column(name = "PHOTOTAG_COUNT", nullable = false)
	public long getPhotoTagCount() {
		return photoTagCount;
	}

	public void setPhotoTagCount(long photoTagCount) {
		this.photoTagCount = photoTagCount;
	}
	
	@Column(name = "POSTTAG_COUNT", nullable = false)
	public long getPostTagCount() {
		return postTagCount;
	}

	public void setPostTagCount(long postTagCount) {
		this.postTagCount = postTagCount;
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
	public Set<PostGrade> getPostGrade() {
		return postGrades;
	}
	
	public void setPostGrade(Set<PostGrade> postGrades) {
		this.postGrades = postGrades;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<PostTag> getPostTag() {
		return postTags;
	}

	public void setPostTag(Set<PostTag> postTags) {
		this.postTags = postTags;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	public Set<PhotoTag> getPhotoTags() {
		return photoTags;
	}

	public void setPhotoTags(Set<PhotoTag> photoTags) {
		this.photoTags = photoTags;
	}

	@Column(name = "CHECKSUM", nullable = true)
	public Boolean getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(Boolean checkSum) {
		this.checkSum = checkSum;
	}

	

	
	
	
	
	
	
	

}
