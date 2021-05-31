package com.bic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.bic.dto.AuthenticationRequest;
import com.bic.entity.User;
import com.bic.repository.UserRepository;

@Service
public class AuthenticateUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    
    public void authenticate(AuthenticationRequest authRequest) throws Exception {
	authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
    }
    
    public User findByUserName(String username) {
    	User user =userRepository.findByUserName(username);
    	if(null != user && !user.getUserName().trim().isEmpty() && !user.getRoles().trim().isEmpty())
    		return user;
    	return null;
    }
}
