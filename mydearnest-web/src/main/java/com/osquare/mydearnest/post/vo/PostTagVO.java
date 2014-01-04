package com.osquare.mydearnest.post.vo;

/**
 * @memo
 * 현재 TagCategory테이블과 헷갈려서 VO 객체들 미완성상태.
 * 추가해야함.
 * 
 * @author skullacy
 *
 */
public class PostTagVO {
	
	private String title;
	private String value;
	private String type;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Title: "+getTitle()+" Value: "+getValue()+" Type: "+getType();
	}
	
	
}
