package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.Post;


public interface AdminPostService {

	Long sizeOfPost();

	Collection<Post> findPost(Integer page, String order);

	Post disabledPost(long id);

}
