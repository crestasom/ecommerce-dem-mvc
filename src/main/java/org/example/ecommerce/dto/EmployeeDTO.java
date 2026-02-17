package org.example.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Employee Data Transfer Object")
public class EmployeeDTO {
    @NotBlank(message = "Name is required")
    @Schema(description = "Full name of the employee", example = "John Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address of the employee", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Contact is required")
    @Size(min = 10, message = "Contact must be at least 10 digits")
    @Schema(description = "Contact number of the employee", example = "9876543210")
    private String contact;

    @NotBlank(message = "Position is required")
    @Schema(description = "Job position of the employee", example = "Software Engineer")
    private String position;

    @Schema(description = "Filename of the profile picture", example = "profile.jpg")
    private String profilePicture;

    @Schema(description = "Base64 encoded string of the profile picture")
    private String profilePictureBase64;
}
