package com.osquare.mydearnest.account.vo;

import net.sf.json.JSONObject;

public class JoinDefault {

	private String mailAddress;
	private String password;
	private String passwordConfirm;

	private String facebookId;
	private String facebookAccessToken;
	private String facebookSecretToken;

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}

	public String getFacebookSecretToken() {
		return facebookSecretToken;
	}

	public void setFacebookSecretToken(String facebookSecretToken) {
		this.facebookSecretToken = facebookSecretToken;
	}

	public String getSocialData() {
		try {
			JSONObject source = new JSONObject();
			
			if (this.facebookAccessToken != null && !this.facebookAccessToken.isEmpty())
				source.put("facebook_accessToken", this.facebookAccessToken);
	
			if (this.facebookSecretToken != null && !this.facebookSecretToken.isEmpty())
				source.put("facebook_secretToken", this.facebookSecretToken);
	
			return source.toString();
		}
		catch(Exception ex) {
			return "";
		}
	}
	
	public void setSocialData(String socialData) {
		try {
			JSONObject source = JSONObject.fromObject(socialData);
		
			if (source.has("facebook_accessToken"))
				this.setFacebookAccessToken(source.getString("facebook_accessToken"));
	
			if (source.has("facebook_secretToken"))
				this.setFacebookSecretToken(source.getString("facebook_secretToken"));
			
		}
		catch(Exception ex) {}
	}

}
