package com.bic.dto;

import java.util.List;

import com.bic.entity.Customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerGetAllStatus extends Status {

	private List<Customer> allCustomer;
}
