package com.bridgelabz.fundoonote.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.fundoonote.dto.User;
import com.bridgelabz.fundoonote.util.TokenUtil;

@Service
public class NotesService implements INotesService{

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	TokenUtil tokenUtil;

	public String fundooNotes(String token) {
		Integer id = tokenUtil.decodeToken(token);
		User user = restTemplate.getForObject("http://localhost:9000/user/getByIdAPI/" + id, User.class);
		if(user.isVerified()) {
			return "Hi " + user.getFName() +" ,Welcome to fundoo application ,in this application we have learned about verfication of user, \n"
					+ "which only works if your verification status is True here is information about your account \n"
					+ " First Name : " + user.getFName() + "\n Last Name : " + user.getLName() 
					+ "\n LMobile Number : " +user.getMobileNumber() + "\n Date of birth : " + user.getDateOfBirth() 
					+ "\n Email : " + user.getEmail() + "\n Password : " +user.getPassword() 
					+ "\n Verification Status : " +user.isVerified() + "\n Date of registration : " + user.getDate();
		}
		else
		{
			return "User is not verified, please verify the user first";
		}		
	}
}
