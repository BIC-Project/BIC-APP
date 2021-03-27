package com.bic.dto;

public class CustomerRegisterStatus extends Status {

    private int registeredCustomerId;

    public int getRegisteredCustomerId() {
	return registeredCustomerId;
    }

    public void setRegisteredCustomerId(int registeredCustomerId) {
	this.registeredCustomerId = registeredCustomerId;
    }

}