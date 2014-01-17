package com.osquare.mydearnest.post.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class PostVO {

	//업로드 과정
	private CommonsMultipartFile thumbnail;
	private String source;
	
	//사진태그 입력시
	private long[] postTagId;
	private String[] title;
	private String[] info;
	private long[] posX1;
	private long[] posY1;
	private long[] posX2;
	private long[] posY2;
	
	//세부입력 과정
		//중복입력 가능한것들 (따로 tag스키마를 사용)
	private long[] tagHome;
	private long[] tagAccessory;
	private String[] tagColor;
		//단일입력해야 하는것들(POST스키마에 입력)
	private long tagSize;
	private long tagTone;
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
	public long[] getPostTagId() {
		return postTagId;
	}
	public void setPostTagId(long[] postTagId) {
		this.postTagId = postTagId;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	public String[] getInfo() {
		return info;
	}
	public void setInfo(String[] info) {
		this.info = info;
	}
	public long[] getPosX1() {
		return posX1;
	}
	public void setPosX1(long[] posX1) {
		this.posX1 = posX1;
	}
	public long[] getPosY1() {
		return posY1;
	}
	public void setPosY1(long[] posY1) {
		this.posY1 = posY1;
	}
	public long[] getPosX2() {
		return posX2;
	}
	public void setPosX2(long[] posX2) {
		this.posX2 = posX2;
	}
	public long[] getPosY2() {
		return posY2;
	}
	public void setPosY2(long[] posY2) {
		this.posY2 = posY2;
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
	public long getTagSize() {
		return tagSize;
	}
	public void setTagSize(long tagSize) {
		this.tagSize = tagSize;
	}
	public long getTagTone() {
		return tagTone;
	}
	public void setTagTone(long tagTone) {
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
