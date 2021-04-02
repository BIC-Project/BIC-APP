package com.bic.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerRegisterStatus extends Status {
	private int registeredCustomerId;
}