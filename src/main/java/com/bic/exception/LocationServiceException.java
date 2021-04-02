package com.bic.exception;

public class LocationServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LocationServiceException() {
		super();
	}

	public LocationServiceException(String message) {
		super(message);
	}

}
