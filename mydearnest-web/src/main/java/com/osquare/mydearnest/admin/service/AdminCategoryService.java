package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.Post;

public interface AdminCategoryService {

	Long sizeOfCategory();

	Collection<Post> findCategory(Integer page);


}