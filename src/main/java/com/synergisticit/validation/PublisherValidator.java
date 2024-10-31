package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Publisher;
@Component
public class PublisherValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {

		return Publisher.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publisherName", "publisherName.value", "publisherName is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "addressLine1.value", "addressLine1 is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine2", "addressLine2.value", "addressLine2 is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.value", "email is empty");
		
	}

}
