

package com.bridgelabz.fundoouser.service;

import java.util.List;

import javax.validation.Valid;

import com.bridgelabz.fundoouser.dto.LoginDTO;
import com.bridgelabz.fundoouser.dto.UserDTO;
import com.bridgelabz.fundoouser.model.User;

public interface IUserService {
    User registerUser(UserDTO dto);

    List<User> getUsers();

    User getById(String token);

    User updateById(String token, UserDTO dto);

    Object deleteById(String token);

    User getByIdAPI(Integer userId);

    User getByEmail(String email);

	User loginUser( LoginDTO dto);

	User changePassword(UserDTO dto);

	User verifyUser(String email);

	List<User> sendEmails();

}
