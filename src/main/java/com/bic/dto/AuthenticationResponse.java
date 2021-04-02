package com.bic.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 6478573300093000889L;
	private final String authToken;

}
