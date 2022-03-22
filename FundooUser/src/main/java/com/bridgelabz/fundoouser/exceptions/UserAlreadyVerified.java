package com.bridgelabz.fundoouser.exceptions;


public class UserAlreadyVerified extends RuntimeException {
    public UserAlreadyVerified(String message) {
        super(message);
    }
}
