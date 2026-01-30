package org.example.ecommerce.validator;

import org.example.ecommerce.service.UserService;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

	private final UserService userService;

	public UniqueEmailValidator(UserService userService) {
		this.userService = userService;
		System.out.println("created UniqueEmailValidator");
	}
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !userService.isUserEmailExists(value);// true -> email exists, false -> email does not exists
	}



}
