package com.bic.exception;

public class CustomerServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomerServiceException() {
	super();
    }

    public CustomerServiceException(String message) {
	super(message);
    }

}