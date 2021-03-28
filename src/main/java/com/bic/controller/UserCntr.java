package com.bic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.AuthenticationRequest;
import com.bic.dto.AuthenticationResponse;
import com.bic.service.AuthenticateUserService;
import com.bic.service.CustomUserDetailsService;
import com.bic.util.AuthJwtUtil;

@RestController
@CrossOrigin
public class UserCntr {

    @Autowired
    private AuthJwtUtil authJwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthenticateUserService authenticateUserService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public String Hello() {
	return "Hello";
    }

    @GetMapping("/hi")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String hi() {
	return "hi";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest authRequest) throws Exception {

	try {
	    authenticateUserService.authenticate(authRequest);
	    final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUserName());
	    final String authToken = authJwtUtil.generateToken(userDetails);
	    return ResponseEntity.ok(new AuthenticationResponse(authToken));
	} catch (Exception e) {
	    throw new BadCredentialsException("Unauthorized");
	}
    }

}
