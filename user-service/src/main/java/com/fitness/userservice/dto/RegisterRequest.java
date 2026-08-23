package com.fitness.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6,message = "Password must contain atleast 6 character")
    private String password;
}
