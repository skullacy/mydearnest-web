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
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IMAGE_SOURCE")
public class ImageSource implements Serializable {

	private long id;
	private String fileName;
	private String extension;
	private String storagePath;
	private long byteLength;
	private String contentType;
	private String aveColor;
	private int width;
	private int height;
	private Date createdAt;
	
	private Set<Post> posts = new HashSet<Post>(0);
	
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

	@Column(name = "FILE_NAME", nullable = false)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "EXTENSION", nullable = false)
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Column(name = "STORAGE_PATH", nullable = false)
	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	@Column(name = "BYTE_LENGTH", nullable = false)
	public long getByteLength() {
		return byteLength;
	}

	public void setByteLength(long byteLength) {
		this.byteLength = byteLength;
	}

	@Column(name = "CONTENT_TYPE", nullable = false)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Column(name = "AVE_COLOR", nullable = true)
	public String getAveColor() {
		return aveColor;
	}

	public void setAveColor(String aveColor) {
		this.aveColor = aveColor;
	}

	@Column(name = "WIDTH", nullable = false)
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Column(name = "HEIGHT", nullable = false)
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Column(name = "CREATED_AT", nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "imageSource")
	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
}
