package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.synergisticit.domain.Book;

import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Book b = (Book)target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.value", "title is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn.value", "isbn is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publicationDate", "publicationDate.value", "publicationDate is empty");
		
		if (b.getBookAuthors().size() == 0) {
			errors.rejectValue("bookAuthors", "bookAuthors.value", "Please add author");
		}
		
	}
}
