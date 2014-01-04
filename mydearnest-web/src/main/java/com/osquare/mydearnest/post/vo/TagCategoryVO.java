package com.osquare.mydearnest.post.vo;

public class TagCategoryVO {
	
	private String title;
	private String type;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Title: "+getTitle()+" Type: "+getType();
	}
	
	
}
