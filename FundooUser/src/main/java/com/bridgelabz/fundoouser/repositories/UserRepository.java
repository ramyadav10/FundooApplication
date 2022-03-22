package com.bridgelabz.fundoouser.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundoouser.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(
        value = "SELECT * from user_details WHERE email= :email",
        nativeQuery = true
    )
    Optional<User> findByEmail(String email);
    
    @Query(
            value = "SELECT email from user_details",
            nativeQuery = true
        )
	List<String> findAllEmails();
    
    @Query(
            value = "SELECT email from user_details",
            nativeQuery = true
        )
	List<User> findAllEmailUsers();
   
}