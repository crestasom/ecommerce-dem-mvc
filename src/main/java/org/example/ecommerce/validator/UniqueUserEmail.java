package org.example.ecommerce.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD) // IMPORTANT: class level
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueUserEmail {
	String message() default "Email already exists";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}


