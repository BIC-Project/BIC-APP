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

		customer.setIsActive(true);
		customer.setCustomerName(
				customer.getCustomerName().trim().toLowerCase());
		customer.setCustomerPinCode(customer.getCustomerPinCode().trim());
		customer.setContactName1(customer.getContactName1().trim());
		if (customer.getContactName2() != null
				&& !customer.getContactName2().trim().isEmpty())
			customer.setContactName2(customer.getContactName2().trim());
		else
			customer.setContactName2(null);
		customer.setContactNumber1(customer.getContactNumber1().trim());
		if (customer.getContactNumber2() != null
				&& !customer.getContactNumber2().trim().isEmpty())
			customer.setContactNumber2(customer.getContactNumber2().trim());
		else
			customer.setContactNumber2(null);
		customer.setCustomerAddress(customer.getCustomerAddress().trim());

		if (customerRepository.existsByCustomerName(customer.getCustomerName()))
			throw new CustomerServiceException("Customer name already exists!");
		Customer custObj = customerRepository.save(customer);
		return custObj.getCustomerId();
	}

	public Customer get(int customerId) throws CustomerServiceException {

		Optional<Customer> customer = customerRepository.findById(customerId);
		if (!customer.isPresent())
			throw new CustomerServiceException("Illegal customer id!");
		return customer.get();
	}

	public List<Customer> getAll() {
		List<Customer> allCustomer = customerRepository
				.findAllByOrderByCustomerNameAsc();
		if (allCustomer == null || allCustomer.isEmpty())
			throw new CustomerServiceException("No customer found.");
		return allCustomer;
	}

	public void update(Customer customer) throws CustomerServiceException {
		System.out.println(customer);
		if (Integer.valueOf(customer.getCustomerId()) == null
				|| customer.getCustomerName() == null
				|| customer.getCustomerName().trim().isEmpty())
			throw new CustomerServiceException(
					"Error! Customer id/name not present!");
		if (customer.getIsActive() == null)
			throw new CustomerServiceException(
					"Error! Please provide valid customer status.");
		System.out.println(customer.getIsActive());
		customer.setCustomerName(customer.getCustomerName().trim());
		customer.setCustomerPinCode(customer.getCustomerPinCode().trim());
		customer.setContactName1(customer.getContactName1().trim());
		if (customer.getContactName2() != null
				&& !customer.getContactName2().trim().isEmpty())
			customer.setContactName2(customer.getContactName2().trim());
		else
			customer.setContactName2(null);
		customer.setContactNumber1(customer.getContactNumber1().trim());
		if (customer.getContactNumber2() != null
				&& !customer.getContactNumber2().trim().isEmpty())
			customer.setContactNumber2(customer.getContactNumber2().trim());
		else
			customer.setContactNumber2(null);

		customer.setCustomerAddress(customer.getCustomerAddress().trim());

		Optional<Customer> oldCustomerOpt = customerRepository
				.findById(customer.getCustomerId());

		if (!oldCustomerOpt.isPresent())
			throw new CustomerServiceException("Illegal customer id.");

		Customer oldCustomer = oldCustomerOpt.get();
		if (!oldCustomer.getCustomerName().trim().toLowerCase()
				.equalsIgnoreCase(
						customer.getCustomerName().trim().toLowerCase()))
			throw new CustomerServiceException("Cannot change customer name!");

		customerRepository.save(customer);
	}
}