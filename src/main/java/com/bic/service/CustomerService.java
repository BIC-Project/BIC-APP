package com.bic.service;

import java.util.List;
import java.util.Optional;

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
	if (!customerRepository.existsByCustomerName(customer.getCustomerName())) {
	    customer.setActive(true);
	    Customer custObj = customerRepository.save(customer);
	    return custObj.getCustomerId();
	    // suppose we want to send an email confirmation
	    // then that code will be here..
	} else
	    throw new CustomerServiceException("Customer Is Already Registered!");
    }

    public Customer get(int customerId) throws CustomerServiceException {

		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer == null)
		    throw new CustomerServiceException("Customer Not Found");
		return customer.get();
    }

    public List<Customer> getAll() {
    	return customerRepository.findAllByOrderByCustomerNameAsc();
    }

    public void update(Customer customer) throws CustomerServiceException {
		if (customerRepository.existsById(customer.getCustomerId())) {
		    if (customerRepository.existsByCustomerName(customer.getCustomerName())) {
		    	throw new CustomerServiceException("Customer Name Already Present");
		    } else
		    	customerRepository.save(customer);
		} else {
		    throw new CustomerServiceException("Customer ID Already Present");
		}
    }

}