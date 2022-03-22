package com.bridgelabz.fundoouser.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoouser.dto.LoginDTO;
import com.bridgelabz.fundoouser.dto.ResponseDTO;
import com.bridgelabz.fundoouser.dto.UserDTO;
import com.bridgelabz.fundoouser.model.User;
import com.bridgelabz.fundoouser.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    IUserService service;

    public UserController() {
    }
    
    //to register user
    @PostMapping({"/register"})
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody UserDTO dto) {
        User user = this.service.registerUser(dto);
        ResponseDTO response = new ResponseDTO("User Registered to bookstore", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    //to login in book store app
  	@GetMapping("/login")
  	public ResponseEntity<ResponseDTO> loginUser(@Valid @RequestBody LoginDTO dto){
  		ResponseDTO response = new ResponseDTO("User Login successefully: ", service.loginUser(dto));
  		return new ResponseEntity<ResponseDTO>(response,HttpStatus.OK);
  	}
  	
  	//to reset password of user
  	@PutMapping("/resetpassword")
  	public ResponseEntity<ResponseDTO> changePassword(@Valid @RequestBody UserDTO dto){
  		ResponseDTO response = new ResponseDTO("User Login :", service.changePassword(dto));
  		return new ResponseEntity<ResponseDTO>(response,HttpStatus.OK);
  	}
  	
  	//to login in book store app
  	@GetMapping("/verify/{token}")
  	public ResponseEntity<ResponseDTO> verifyUser(@PathVariable String token){
  		ResponseDTO response = new ResponseDTO("User verfication :", service.verifyUser(token));
  		return new ResponseEntity<ResponseDTO>(response,HttpStatus.OK);
  	}
  	
  	//to retrieve all users
    @GetMapping({"/getAll"})
    public ResponseEntity<String> getUsers() {
        List<User> user = this.service.getUsers();
        ResponseDTO response = new ResponseDTO("Users :", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    //rabbitMQ to send mails to all register users
    @GetMapping("/sendEmails")
    public ResponseEntity<ResponseDTO> sendEmails() {
        List<User> email = service.sendEmails();
        ResponseDTO response = new ResponseDTO("emails :", email);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //to get specific user using token provided
    @GetMapping({"/findById/{token}"})
    public ResponseEntity<ResponseDTO> getById(@PathVariable String token) {
        User user = this.service.getById(token);
        ResponseDTO response = new ResponseDTO("Requested User : ", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    //to get specific user using email
    @GetMapping({"/getByEmail/{email}"})
    public ResponseEntity<ResponseDTO> getByEmail(@PathVariable String email) {
        User user = this.service.getByEmail(email);
        ResponseDTO response = new ResponseDTO("Requested User : ", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //to update specific user using token provided
    @PutMapping({"/update/{token}"})
    public ResponseEntity<ResponseDTO> updateById(@PathVariable String token, @Valid @RequestBody UserDTO dto) {
        User user = this.service.updateById(token, dto);
        ResponseDTO response = new ResponseDTO("User updated : ", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    //to delete specific user using token provided
    @DeleteMapping({"/delete/{token}"})
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String token) {
        ResponseDTO response = new ResponseDTO("User deleted successfully", this.service.deleteById(token));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    //--------------rest template api------------------//
    
    //to retrieve user by id
    @GetMapping({"/getByIdAPI/{userId}"})
    public User getByIdAPI(@PathVariable Integer userId) {
        System.out.println("Test");
        User user = this.service.getByIdAPI(userId);
        return user;
    }
}
