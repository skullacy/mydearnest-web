package com.osquare.mydearnest.util.amazon;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.junglebird.webframe.common.PropertiesManager;

@Component
public class MdnAmazonManager {
	
	@Autowired private PropertiesManager pm;

	AWSCredentials credentials;
	
	public AmazonS3Client getAmazonS3() {
		if(this.credentials == null) setCredential();
		return new AmazonS3Client(this.credentials);
	}
	
	private void setCredential() {
		this.credentials = new BasicAWSCredentials(pm.get("amazon.credential.accessKey"), pm.get("amazon.credential.secretKey"));
	}
	

}
