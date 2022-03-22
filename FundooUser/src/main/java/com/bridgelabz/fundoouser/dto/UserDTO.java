package com.bridgelabz.fundoouser.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserDTO {
    @Pattern(
        regexp = "^[A-Z]{1}[a-zA-Z\\s]{2,}$",
        message = "First letter of first name must be capital"
    )
    private String fName;
    @Pattern(
        regexp = "^[A-Z]{1}[a-zA-Z\\s]{2,}$",
        message = "First letter of last name must be capital"
    )
    private String lName;
    @Pattern(
        regexp = "[0-9]{10}",
        message = "Invalid Mobile Number"
    )
    private String mobileNumber;
    @Email(
        message = "Insert valid email"
    )
    private String email;   
    private String password;
    @Pattern
    (
    	regexp = "^\\d{4}-\\d{2}-\\d{2}$",
    	message = "date of vbirth must be in YYYY-MM-DD format"
   	)
    private String dateOfBirth;  

    public UserDTO() {
    }
}