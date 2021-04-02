package com.bic.exception;

public class StockServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StockServiceException() {
		super();
	}

	public StockServiceException(String message) {
		super(message);
	}

}
