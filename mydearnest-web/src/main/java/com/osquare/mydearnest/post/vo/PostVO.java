package com.osquare.mydearnest.post.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class PostVO {

	private CommonsMultipartFile thumbnail;
	private Long category;
	
	private String position;
	
	private String title;
	private String desc;
	private String source;
	
	private int homeSize;
	private int areaType;
	private String accessory;
	
	private float feelModern;
	private float feelLuxury;
	private float feelWarm;
	private float feelVintage;
	private float feelCute;

	public CommonsMultipartFile getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(CommonsMultipartFile thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	public int getHomeSize() {
		return homeSize;
	}

	public void setHomeSize(int homeSize) {
		this.homeSize = homeSize;
	}

	public int getAreaType() {
		return areaType;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public float getFeelModern() {
		return feelModern;
	}

	public void setFeelModern(float feelModern) {
		this.feelModern = feelModern;
	}

	public float getFeelLuxury() {
		return feelLuxury;
	}

	public void setFeelLuxury(float feelLuxury) {
		this.feelLuxury = feelLuxury;
	}

	public float getFeelWarm() {
		return feelWarm;
	}

	public void setFeelWarm(float feelWarm) {
		this.feelWarm = feelWarm;
	}

	public float getFeelVintage() {
		return feelVintage;
	}

	public void setFeelVintage(float feelVintage) {
		this.feelVintage = feelVintage;
	}

	public float getFeelCute() {
		return feelCute;
	}

	public void setFeelCute(float feelCute) {
		this.feelCute = feelCute;
	}

	

}
