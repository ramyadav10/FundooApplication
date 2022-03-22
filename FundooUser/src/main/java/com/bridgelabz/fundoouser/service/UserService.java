package com.bridgelabz.fundoouser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoouser.dto.LoginDTO;
import com.bridgelabz.fundoouser.dto.UserDTO;
import com.bridgelabz.fundoouser.exceptions.PasswordException;
import com.bridgelabz.fundoouser.exceptions.UserAlreadyVerified;
import com.bridgelabz.fundoouser.exceptions.UserException;
import com.bridgelabz.fundoouser.exceptions.UserNotVerified;
import com.bridgelabz.fundoouser.model.User;
import com.bridgelabz.fundoouser.rabbitmqconfig.RabbitMqConfig;
import com.bridgelabz.fundoouser.repositories.UserRepository;
import com.bridgelabz.fundoouser.util.EmailSenderService;
import com.bridgelabz.fundoouser.util.TokenUtil;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository repo;
    
    @Autowired
    EmailSenderService sender;
    
    @Autowired
    TokenUtil tokenUtil;
    
    @Autowired
    RabbitTemplate rabbitTemplate;

    
    //to register user 
    public User registerUser(UserDTO dto) {
        Optional<User> matcher = repo.findByEmail(dto.getEmail());
        if (matcher.isPresent()) 
        {
            throw new UserException("Email Already Registered");
        } 
        else 
        {
	        User user = new User(dto);
	        this.repo.save(user);
	        String token = this.tokenUtil.createToken(user.getUserId());
	        this.sender.sendEmail(user.getEmail(), "User successfully registered", "Hi, " + user.getFName() + " Welcome to fundoo application \n"
	        		+ "for verification click here : http://localhost:9000/user/verify/" + tokenUtil.createToken(user.getUserId()) +" \n"
		            +"\n click on following link to retrieve data : \n http://localhost:9000/user/findById/" 
		            + token);
	        return user;
       }
    }

    
	//to log in into book store app (with if else)
  	@Override
  	public User loginUser(LoginDTO dto) {
  		Optional<User> user = repo.findByEmail(dto.getEmail());
  		if(!user.get().isVerified()) 
  		{
  			throw new UserNotVerified("check your email and verify your account");
  		}
  		else
  		{
	  		if(user.get().equals(null)) {
	  			throw new UserException("There are no users with given email id");
	  		}
	  		else
	  		{
		  		if(!dto.getPassword().equals(user.get().getPassword())) {
		  			throw new PasswordException("Invalid password");
		  		}
		  		else
		  		{
			  		sender.sendEmail(user.get().getEmail(), "User successfully login", "Hi, " + user.get().getFName()				
			  				+ " Welcome to fundoo application \n"
			  				+ "to get account information : \n"
			  				+ " http://localhost:9000/user/findById/" + tokenUtil.createToken(user.get().getUserId()) + " \n"
			  				+ "\n to update account information : \n http://localhost:9000/user/update/" 
			  				+ tokenUtil.createToken(user.get().getUserId())
			  				+ "\n to delete account information : \n http://localhost:9000/user/delete/" 
			  				+ tokenUtil.createToken(user.get().getUserId()));
			  		return user.get();
		  		}
	  		}
  		}
  	}	
  	
  	
  	//to log in into fundoo app
  	public User changePassword(UserDTO dto) {
  		Optional<User> user = repo.findByEmail(dto.getEmail());
  		if(user.equals(null)) {
  			throw new UserException("There are no users with given id");
  		}
  		user.get().setPassword(dto.getPassword());
  		repo.save(user.get());
  		sender.sendEmail(user.get().getEmail(), "password successfully changed", "someone just logged in,in your account");
  		return user.get(); 
  	}
  	
  	//to verify user (with else if)
  	public User verifyUser(String token) {
  		Integer id = tokenUtil.decodeToken(token);
  		Optional<User> user = repo.findById(id);
  		if(user.isEmpty()) {
  			throw new UserException("There are no users with given email id");
  		}
  		else if(user.get().isVerified()) 
		{
			throw new UserAlreadyVerified("no need to verify user again");
		}
		else
		{
			user.get().setVerified(true);
			repo.save(user.get());
			sender.sendEmail(user.get().getEmail(),"verification", "hi " + user.get().getFName() + " ,Welcome to fundoo application.. /n"
					+ "your account is verified. \n to log in, in application click herer : \n"
					+ "http://localhost:9000/user/login");
			return user.get();
		}
	}
    
    //to retrieve list of all users
    public List<User> getUsers() {
        List<User> list = repo.findAll();
        if (list.isEmpty()) {
            throw new UserException("There are no users added");
        } else {
            return list;
        }
    }
    
    //method used to send email
    public String mailSender(User user) {
    	if (user.isVerified()) {
    		sender.sendEmail(user.getEmail(),"New year new offer","Hello "+ user.getFName() +", \n 50% off on all electronics");
        	return "Mail send to : " + user.getEmail();	
    	}
    	else
    	{
    		sender.sendEmail(user.getEmail(),"New year new offer","Hello "+ user.getFName() +", \n 50% off on all electronics"
    				+ " to access this offer verify your account first \n"
    				+ "for verification click here : http://localhost:9000/user/verify/" + tokenUtil.createToken(user.getUserId()) );
        	return "Mail send to : " + user.getEmail();	
    	}
    } 
    
    //rabbitMQ for sending mail
	public List<User> sendEmails() {
		List<User> list = repo.findAll();
    	if (list.isEmpty()) {
            throw new UserException("There are no users added");
        } 
    	else 
    	{
        	list.forEach(user -> {rabbitTemplate
        		.convertAndSend(RabbitMqConfig.ROUTING_KEY,RabbitMqConfig.EXCHANGE, mailSender(user));});
            return list;
        }
	}

    
    //to retrieve certain user using token as id
    public User getById(String token) {
        Integer userId = this.tokenUtil.decodeToken(token);
        Optional<User> user = this.repo.findById(userId);
        if (user.isEmpty()) {
            throw new UserException("There are no users with given id");
        } 
        else 
        {
            this.sender.sendEmail(((User)user.get()).getEmail(), "User successfully retrieved", "for User : \n" 
            		+ user + "\n click on following link to retrieve data : \n http://localhost:9000/user/findbyid/" 
            		+ token);
            return (User)user.get();
        }
    }
    
    //to retrieve certain user using email
    public User getByEmail(String email) {
        Optional<User> user = this.repo.findByEmail(email);
        if (user.isEmpty()) 
        {
            throw new UserException("There are no users with given id");
        }
        else 
        {
            this.sender.sendEmail(((User)user.get()).getEmail(), "User successfully retrieved", "for User : \n" 
            		+ user + "\n click on following link to retrieve data : \n http://localhost:9000/user/findbyid/"
            		+ this.tokenUtil.createToken(((User)user.get()).getUserId()));
            return (User)user.get();
        }
    }
    
    //method used in updateById service
    public User updateFunction(String token, UserDTO dto) {
        Integer userId = tokenUtil.decodeToken(token);
        Optional<User> user = repo.findById(userId);
        if (user.isPresent()) {
            User newUser = new User(userId, dto);
            newUser.setEmail(dto.getEmail());
            this.repo.save(newUser);
            this.sender.sendEmail(((User)user.get()).getEmail(), "User successfully updated", "for User : \n" 
            		+ user + "\n click on following link to retrieve data : \n http://localhost:9000/user/findbyid/" 
            		+ token);
            return newUser;
        } 
        else 
        {
            throw new UserException("Employee not found");
        }
    }
    
    //to update user checking if email exists and update
    public User updateById(String token, UserDTO dto) {
        Optional<User> matcher = repo.findByEmail(dto.getEmail());
        if (((User)matcher.get()).getEmail().equals(dto.getEmail())) {
            this.updateFunction(token, dto);
            return this.updateFunction(token, dto);
        } else if (!matcher.isEmpty()) {
            throw new UserException("Email Already Registered");
        } else {
            return this.updateFunction(token, dto);
        }
    }
    
    //to delete user by id
    public Object deleteById(String token) {
        Integer userId = this.tokenUtil.decodeToken(token);
        Optional<User> user = this.repo.findById(userId);
        if (user.isEmpty()) {
            throw new UserException("Invalid token..please input valid token");
        } else {
            this.sender.sendEmail(((User)user.get()).getEmail(), "User successfully deleted", "User : \n" + user.get());
            this.repo.deleteById(userId);
            return user.get();
        }
    }
    
    //---------------------------Rest template-------------------------//
    
    public User getByIdAPI(Integer userId) {
        Optional<User> user = this.repo.findById(userId);
        if (user.isEmpty()) {
            throw new UserException("There are no users with given id, invalid token");
        } else {
            return (User)user.get();
        }
    }


	
	
}
