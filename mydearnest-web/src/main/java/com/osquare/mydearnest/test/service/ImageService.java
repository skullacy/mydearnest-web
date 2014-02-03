package com.osquare.mydearnest.test.service;

import java.net.URL;
import java.util.List;

import com.osquare.mydearnest.entity.ImageSource;

public interface ImageService {
	List<ImageSource> getImageSourceList();
	boolean updateAvgColor(long imageId);
	String getAvgColor(URL img);
	boolean deleteThumbs();
	boolean deleteThumbs(long postId);
	boolean copySources();
}
