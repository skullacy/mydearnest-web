package com.osquare.mydearnest.post.vo;

import com.osquare.mydearnest.api.vo.ResultObject;

@SuppressWarnings("serial")
public class ProfileImageQue extends ResultObject {

	private Long imageId;
	
	public Long getImageId(){ return this.imageId; }
	public void setImageId(Long imageId){ this.imageId = imageId; }

}
