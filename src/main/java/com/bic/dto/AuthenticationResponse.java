package com.bic.dto;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 6478573300093000889L;
    private final String authToken;

    public AuthenticationResponse(String authToken) {
	this.authToken = authToken;
    }

    public String getAuthToken() {
	return authToken;
    }
}
