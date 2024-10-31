package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Librarian;
@Component
public class LibrarianValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Librarian.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.value", "firstName is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.value", "lastName is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone.value", "phone is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.value", "email is empty");
	}

}
