package com.bridgelabz.fundoonote.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonote.dto.ResponseDTO;
import com.bridgelabz.fundoonote.service.INotesService;

@RestController
@RequestMapping("/notes")
public class FundooController {
	
	@Autowired
	INotesService service;
	
	//to provide message from fundoo
	@GetMapping("/{token}")
	public ResponseEntity<ResponseDTO> fundooNotes(@PathVariable String token){
		String notes = service.fundooNotes(token);
		ResponseDTO response = new ResponseDTO("notes provided by Fundoo ",notes);
		return new ResponseEntity<ResponseDTO>(response,HttpStatus.OK);
	}
}
