package com.osquare.mydearnest.post.service;

import java.util.Collection;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.osquare.mydearnest.entity.Folder;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.vo.ImageSourceFile;


public interface FileService {

	ImageSource getImageSource(Long imageId);
	ImageSource createImageSourceForURL(String imageUrl);
	ImageSource createImageSourceForData(CommonsMultipartFile filedata);
	
	ImageSourceFile getSourceFile(ImageSource imageSource);
	ImageSourceFile getSourceFile(ImageSource imageSource, Long width, Long height, String thumbnail_type);
	ImageSourceFile getSourceFile(ImageSource imageSource, Long width, Long height, String thumbnail_type, Long postId);
	
	Collection<ImageSource> getImageSourceByFolder(Folder folder, int maxCount);
	Collection<ImageSource> getImageSourceByReaded(long accountId, int maxCount);

	void cropImageSource(Post post, ImageSource imageSource, String crop);

}
