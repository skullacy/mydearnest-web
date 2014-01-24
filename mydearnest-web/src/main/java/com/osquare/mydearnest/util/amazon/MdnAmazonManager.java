package com.osquare.mydearnest.util.amazon;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.junglebird.webframe.common.PropertiesManager;

public class MdnAmazonManager {
	@Autowired private PropertiesManager pm;
	
	AWSCredentials credentials;
	
	public MdnAmazonManager() {
		System.out.println(pm);
		System.out.println("AccessKey: " + pm.get("amazon.credential.accessKey") +"    SecretKey: " + pm.get("amazon.credential.secretKey"));
		this.credentials = new BasicAWSCredentials(pm.get("amazon.credential.accessKey"), pm.get("amazon.credential.secretKey"));
	}
	
	public AmazonS3Client getAmazonS3() {
		return new AmazonS3Client(this.credentials);
	}
	

}
