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
		if (!customerRepository
				.existsByCustomerName(customer.getCustomerName())) {
			customer.setActive(true);
			Customer custObj = customerRepository.save(customer);
			return custObj.getCustomerId();
			// suppose we want to send an email confirmation
			// then that code will be here..
		} else
			throw new CustomerServiceException(
					"Customer Name is Already Exists!");
	}

	public Customer get(int customerId) throws CustomerServiceException {

		Optional<Customer> customer = customerRepository.findById(customerId);
		if (!customer.isPresent())
			throw new CustomerServiceException("Illegal Customer Id!");
		return customer.get();
	}

	public List<Customer> getAll() {
		List<Customer> allCustomer = customerRepository
				.findAllByOrderByCustomerNameAsc();
		if (allCustomer == null || allCustomer.isEmpty())
			throw new CustomerServiceException("Customers not found");
		return allCustomer;
	}

	public void update(Customer customer) throws CustomerServiceException {

		if (Integer.valueOf(customer.getCustomerId()) == null
				|| customer.getCustomerName() == null
				|| customer.getCustomerName().trim().isEmpty())
			throw new CustomerServiceException(
					"Error! Customer Id/Name not present!");

		Optional<Customer> oldCustomerOpt = customerRepository
				.findById(customer.getCustomerId());

		if (!oldCustomerOpt.isPresent())
			throw new CustomerServiceException("Illegel Customer Id");

		Customer oldCustomer = oldCustomerOpt.get();
		if (!oldCustomer.getCustomerName()
				.equalsIgnoreCase(customer.getCustomerName()))
			throw new CustomerServiceException("Cannot Change Customer Name!");

		customerRepository.save(customer);

	}

}