package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.vo.TagCategoryVO;



public interface AdminTagCateService {

	//태그 갯수 구하기 
	Long sizeOfTag(String type);
	
	Collection<TagCategory> findTag(Integer page, String type, String order);

	TagCategory disabledTag(long id);
	
	//태그 정보 가져오기
	TagCategory getTagInfo(long id);
	
	//태그 생성하기
	TagCategory createTagCategory(TagCategoryVO tagCateVO);
	TagCategory updateTagCategory(TagCategoryVO tagCateVO, long tagCateId);
	
	//Autocomplete에서 검색되는 태그들 리턴해주기
	Collection<TagCategory> getMatchedTagCategories(String keyword, String type);
	
	//모든 태그 리턴
	Collection<TagCategory> getTagCategories();

}
