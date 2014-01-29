package com.osquare.mydearnest.test.service;

import java.util.List;

import com.osquare.mydearnest.entity.ImageSource;

public interface ImageDAO {
	List<ImageSource> getImageSourceList();
	boolean updateAvgColor(ImageSource imageSource);
}
