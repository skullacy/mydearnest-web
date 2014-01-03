package com.osquare.mydearnest.post.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.osquare.mydearnest.post.vo.PostVO;

public class PostUploadValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return PostVO.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {

//		ValidationUtils.rejectIfEmpty(errors, "desc", "error.required.description");
//		ValidationUtils.rejectIfEmpty(errors, "category", "error.required.category");
		
		PostVO postVO = (PostVO) target;
	}
}
