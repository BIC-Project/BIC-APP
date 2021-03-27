package com.bic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.bic.dto.AuthenticationRequest;
import com.bic.util.AuthJwtUtil;

@Service
public class AuthenticateUserService {

    @Autowired
    private AuthJwtUtil authJwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    public void authenticate(AuthenticationRequest authRequest) throws Exception {
	try {
	    authenticationManager.authenticate(
		    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
	} catch (BadCredentialsException ex) {
	    throw new Exception("Inavalid UserName/Password");
	}

    }
}
