package com.dexturn.jobportal.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ContactRequestDto(

        @NotBlank(message = "Email can not be Empty")
        @Email(message = "Invalid email address")
        String email,


        @NotBlank(message = "Message can not be Empty")
        @Size(min = 5, max = 500, message = "Message must be between 5 and 500 characters")
        String message,

        @NotBlank(message = "Name can not be Empty")
        @Size(min = 5, max = 30, message = "Name must be between 5 and 30")
        String name,

        @Size(message = "Subject must be between 5 and 150 characters")
        @NotBlank(message = "Subject can not be Empty")
        String subject,


        @Pattern(regexp = "Job Seeker|Employer|Other")
        @NotBlank(message = "UserType can not be Empty")
        String userType)implements Serializable {
}
