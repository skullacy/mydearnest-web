package com.osquare.mydearnest.post.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.Category;


public interface CategoryService {

	Collection<Category> getRootCategories();
	Collection<Category> getMatchedCategories(String keyword);
	Collection<Category> getChildCategories(Long id);
}
