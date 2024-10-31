package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.User;

@Component
public class UserValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User u = (User) target;
		
		if (u.getUsername() == null) {
			errors.rejectValue("username", "username.value","username is required");
		}
		
		if (u.getUserPassword() == null) {
			errors.rejectValue("userPassword", "userPassword.value","Password is required");
		}
		
		if (u.getUserRoles().size() < 1) {
			errors.rejectValue("userRoles", "userRoles.value", "Please select roles");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.value", "username is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userPassword", "userPassword.value", "userPassword is empty");
	}

}
