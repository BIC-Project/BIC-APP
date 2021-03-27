package com.bic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.bic.dto.AuthenticationRequest;

@Service
public class AuthenticateUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    public void authenticate(AuthenticationRequest authRequest) throws Exception {

	authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

    }
}
