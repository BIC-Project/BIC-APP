package com.bic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.entity.Customer;
import com.bic.exception.CustomerServiceException;
import com.bic.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public int register(Customer customer) throws CustomerServiceException {
	if (!customerRepository.isCustomerPresent(customer.getCustomerName())) {
	    customer.setActive(true);
	    return customerRepository.save(customer);
	    // suppose we want to send an email confirmation
	    // then that code will be here..
	} else
	    throw new CustomerServiceException("Customer Is Already Registered!");
    }

    public Customer get(int customerId) throws CustomerServiceException {

	Customer customer = customerRepository.fetch(customerId);
	if (customer == null)
	    throw new CustomerServiceException("Customer Not Found");
	return customer;
    }

//	public List<Customer> getAll() {
//		return customerRepository.findAll();
//	}
}