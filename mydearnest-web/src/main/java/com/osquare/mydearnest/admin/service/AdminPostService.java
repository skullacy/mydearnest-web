package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;


public interface AdminPostService {

	Long sizeOfPost();

	Collection<Post> findPost(Integer page, String order, Integer checksum, Account account);
	
	Post disabledPost(long id);

}
