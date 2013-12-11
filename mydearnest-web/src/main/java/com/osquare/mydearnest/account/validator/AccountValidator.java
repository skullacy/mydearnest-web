package com.osquare.mydearnest.account.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.osquare.mydearnest.account.vo.AccountDetails;
import com.osquare.mydearnest.account.vo.JoinDefault;

public class AccountValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return JoinDefault.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		
		if (target.getClass().equals(JoinDefault.class)) {
			ValidationUtils.rejectIfEmpty(errors, "mailAddress", "error.required.mail_address");
			ValidationUtils.rejectIfEmpty(errors, "password", "error.required.password");
			ValidationUtils.rejectIfEmpty(errors, "passwordConfirm", "error.required.password_confirm");
	
			JoinDefault account = (JoinDefault) target;
			if (account.getPassword().length() < 6)
				errors.rejectValue("password", "error.valid.password.tooshort");
	
			if(account.getMailAddress() != null) {
				String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
				Matcher m = Pattern.compile(regex).matcher(account.getMailAddress());
				if (!m.matches()) errors.rejectValue("mailAddress", "error.valid.mail_address");
			}
		}
		else if (target.getClass().equals(AccountDetails.class)) {
			ValidationUtils.rejectIfEmpty(errors, "username", "error.required.username");
			ValidationUtils.rejectIfEmpty(errors, "agree", "error.required.agree");
		}

	}

}
