package com.bic.dto;

import com.bic.entity.Customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerGetStatus extends Status {
	private Customer customer;
}
