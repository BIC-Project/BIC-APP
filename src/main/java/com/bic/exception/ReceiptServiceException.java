package com.bic.exception;

public class ReceiptServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReceiptServiceException() {
		super();
	}

	public ReceiptServiceException(String message) {
		super(message);
	}

}
