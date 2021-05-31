package com.bic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.AuthenticationRequest;
import com.bic.dto.AuthenticationStatus;
import com.bic.dto.Status.StatusType;
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
	
	@Value("${app.expiry.time}")
	private Long expTime;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationStatus> generateToken(
			@RequestBody AuthenticationRequest authRequest) throws Exception {
		AuthenticationStatus authenticationStatus = new AuthenticationStatus();
		try {
			authenticateUserService.authenticate(authRequest);
			final UserDetails userDetails = customUserDetailsService
					.loadUserByUsername(authRequest.getUserName());
			final String authToken = authJwtUtil.generateToken(userDetails);
			authenticationStatus.setStatus(StatusType.SUCCESS);
			authenticationStatus.setMessage("Login successful!");
			authenticationStatus.setAuthToken(authToken);
			authenticationStatus.setUserName(authRequest.getUserName());
			authenticationStatus.setRoles(authenticateUserService.findByUserName(authRequest.getUserName()).getRoles());
			authenticationStatus.setExpTime(expTime);
			return new ResponseEntity<AuthenticationStatus>(
					authenticationStatus, HttpStatus.OK);
		} catch (Exception e) {
			throw new BadCredentialsException("Unauthorized");
		}
	}

}
