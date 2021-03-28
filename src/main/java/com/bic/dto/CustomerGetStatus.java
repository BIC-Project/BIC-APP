package com.bic.dto;

import com.bic.entity.Customer;

public class CustomerGetStatus extends Status {
    private Customer customer;

    public CustomerGetStatus() {
	super();
	// TODO Auto-generated constructor stub
    }

    public Customer getCustomer() {
	return customer;
    }

    public void setCustomer(Customer customer) {
	this.customer = customer;
    }

    public CustomerGetStatus(Customer customer) {
	super();
	this.customer = customer;
    }
}
