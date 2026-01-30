package org.example.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact is required")
    @Size(min = 10, message = "Contact must be at least 10 digits")
    private String contact;

    @NotBlank(message = "Position is required")
    private String position;

    private String profilePicture;
    private String profilePictureBase64;
}
