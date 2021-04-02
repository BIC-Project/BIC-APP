package com.bic.dto;

import lombok.Data;

@Data
public class Status {

	private StatusType status;
	private String message;

	public static enum StatusType {
		SUCCESS, FAILURE;
	}
}
