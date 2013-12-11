package com.osquare.mydearnest.api.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResultObject implements Serializable {

	private String action;
	private boolean success;
	private String message;
	
	public String getAction(){
		return this.action;
	}
	
	public void setAction(String action){
		this.action = action;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
}
