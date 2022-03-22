package com.bridgelabz.fundoonote.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String fName;
    private String lName;
    private String mobileNumber;
    private String email;
    private String password;;
    private String dateOfBirth;  
    private LocalDate date = LocalDate.now();
    private boolean verified = false;
 

    public User() {
    }
	
}