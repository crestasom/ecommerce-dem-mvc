package org.example.ecommerce.validator;

import org.example.ecommerce.service.UserService;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

	private UserService userService;



	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !userService.isUserEmailExists(value);// true -> email exists, false -> email does not exists
	}



}
