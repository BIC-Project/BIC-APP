package com.bic.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthenticationStatus extends Status {

	private String userName;
	private String roles;
	private Long expTime;
	private String authToken;
	
}
