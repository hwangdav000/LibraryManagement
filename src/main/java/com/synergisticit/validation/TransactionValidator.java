package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Book;
import com.synergisticit.domain.Transaction;
@Component
public class TransactionValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {

		return Transaction.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "transactionDate", "transactionDate.value", "transactionDate is empty");
		
	}

}
