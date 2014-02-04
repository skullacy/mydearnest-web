package com.osquare.mydearnest.test.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.junglebird.webframe.common.PropertiesManager;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.service.FileService;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.util.amazon.MdnAmazonManager;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
	
	@Autowired private ImageDAO imageDAO;
	@Autowired private PostService postService;
	@Autowired private FileService fileService;
	@Autowired private PropertiesManager pm;
	@Autowired private MdnAmazonManager mdnAmazonManager;

	public ImageServiceImpl() {
		
	}

	@Override
	public List<ImageSource> getImageSourceList() {
		return imageDAO.getImageSourceList();
	}

	@Override
	public boolean updateAvgColor(long imageId) {
		ImageSource imageSource = fileService.getImageSource(imageId);
		
		StringBuilder builder = new StringBuilder();
		builder.append(pm.get("amazon.fs.serverUrl")).append("/").append(imageSource.getStoragePath())
		.append("/").append(imageSource.getId()).append("/source");
		
		System.out.println("img URL: " + builder);
		
		try {
			imageSource.setAveColor(getAvgColor(new URL(builder.toString())));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		imageDAO.updateAvgColor(imageSource);
		
		return true;
	}

	@Override
	public String getAvgColor(URL img) {
		String rgbHex = null;
		
		try {
			ResampleOp resampleOp = new ResampleOp(3, 3);
			resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
			BufferedImage tdestImg = resampleOp.filter(ImageIO.read(img), null);
			
			int red = 0;
			int green = 0;
			int blue = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int rgbColor = tdestImg.getRGB(i, j);
					Color color = new Color(rgbColor);
					red += color.getRed();
					green += color.getGreen();
					blue += color.getBlue();
				}
			}
			
	        rgbHex = String.format("#%02x%02x%02x", red/9, green/9, blue/9);
		}
		catch(Exception e) {
			e.printStackTrace();
			
			rgbHex = "#FFFFFF";
		}
		
		return rgbHex;
	}

	@Override
	public boolean deleteThumbs() {
		String bucketName = pm.get("amazon.fs.bucketName");
		
		AmazonS3Client fileStorageServer = mdnAmazonManager.getAmazonS3();
		
		ObjectListing listing = fileStorageServer.listObjects(bucketName);
		List<S3ObjectSummary> keyList = listing.getObjectSummaries();

		deleteThumbObject(fileStorageServer, bucketName, keyList);
		
		return true;
	}
	
	@Override
	public boolean deleteThumbs(long postId) {
		ImageSource imageSource = postService.getPostById(postId).getImageSource();
		
		String bucketName = pm.get("amazon.fs.bucketName");
		
		StringBuilder prefix = new StringBuilder();
		prefix.append(imageSource.getStoragePath()).append("/").append(imageSource.getId()).append("/");
		
		AmazonS3Client fileStorageServer = mdnAmazonManager.getAmazonS3();
		
		ObjectListing listing = fileStorageServer.listObjects(bucketName, prefix.toString());
		List<S3ObjectSummary> keyList = listing.getObjectSummaries();

		deleteThumbObject(fileStorageServer, bucketName, keyList);

		return true;
	}

	public void deleteThumbObject(AmazonS3Client fileStorageServer, String bucketName, List<S3ObjectSummary> keyList) {
		for (S3ObjectSummary s3ObjectSummary : keyList) {
			String key = s3ObjectSummary.getKey();
			
			if (!key.startsWith("source")) {
				fileStorageServer.deleteObject(bucketName, key);
				System.out.println("Deleted file: " + key);
			}
		}
	}

	@Override
	public boolean copySources() {
		String bucketName = pm.get("amazon.fs.bucketName");
		
		AmazonS3Client fileStorageServer = mdnAmazonManager.getAmazonS3();
		
		ObjectListing listing = fileStorageServer.listObjects(bucketName);
		List<S3ObjectSummary> keyList = listing.getObjectSummaries();

		for (S3ObjectSummary s3ObjectSummary : keyList) {
			String key = s3ObjectSummary.getKey();
			
			if (key.endsWith("source")) {
				fileStorageServer.copyObject(bucketName, key, bucketName, "source/" + key);
				System.out.println("Copied file: " + key);
			}
		}
		
		return true;
	}
}
