package org.example.ecommerce.model;

import org.example.ecommerce.validator.UniqueUserEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Name is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
	@UniqueUserEmail(message = "{user.email.unique.error}")
    private String email;
    private String role;
    private String profilePicture;
    private java.time.LocalDate birthDate;

}
