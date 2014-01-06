package com.osquare.mydearnest.post.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class PostVO {

	//업로드 과정
	private CommonsMultipartFile thumbnail;
	private String source;
	
	
	//세부입력 과정
		//중복입력 가능한것들 (따로 tag스키마를 사용)
	private long[] tagHome;
	private long[] tagAccessory;
	private String[] tagColor;
		//단일입력해야 하는것들(POST스키마에 입력)
	private int tagSize;
	private int tagTone;
	private int theme;
	private int spaceType;
	
	
	//평가 과정
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public long[] getTagHome() {
		return tagHome;
	}
	public void setTagHome(long[] tagHome) {
		this.tagHome = tagHome;
	}
	public long[] getTagAccessory() {
		return tagAccessory;
	}
	public void setTagAccessory(long[] tagAccessory) {
		this.tagAccessory = tagAccessory;
	}
	public String[] getTagColor() {
		return tagColor;
	}
	public void setTagColor(String[] tagColor) {
		this.tagColor = tagColor;
	}
	public int getTagSize() {
		return tagSize;
	}
	public void setTagSize(int tagSize) {
		this.tagSize = tagSize;
	}
	public int getTagTone() {
		return tagTone;
	}
	public void setTagTone(int tagTone) {
		this.tagTone = tagTone;
	}
	public int getTheme() {
		return theme;
	}
	public void setTheme(int theme) {
		this.theme = theme;
	}
	public int getSpaceType() {
		return spaceType;
	}
	public void setSpaceType(int spaceType) {
		this.spaceType = spaceType;
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
