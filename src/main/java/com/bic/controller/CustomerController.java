package com.bic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.CustomerGetAllStatus;
import com.bic.dto.CustomerGetStatus;
import com.bic.dto.CustomerRegisterStatus;
import com.bic.dto.Status.StatusType;
import com.bic.entity.Customer;
import com.bic.exception.CustomerServiceException;
import com.bic.service.CustomerService;

@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/customer")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<CustomerRegisterStatus> register(
			@RequestBody Customer customer) {
		try {
			int id = customerService.register(customer);
			CustomerRegisterStatus status = new CustomerRegisterStatus();
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Registration Successful!");
			status.setRegisteredCustomerId(id);
			return new ResponseEntity<CustomerRegisterStatus>(status,
					HttpStatus.CREATED);
		} catch (CustomerServiceException e) {
			CustomerRegisterStatus status = new CustomerRegisterStatus();
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerRegisterStatus>(status,
					HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/customer/{customerId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<CustomerGetStatus> getCustomer(
			@PathVariable int customerId) {
		CustomerGetStatus status = new CustomerGetStatus();
		try {
			Customer customer = customerService.get(customerId);
			status.setCustomer(customer);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Customer Found");
			return new ResponseEntity<CustomerGetStatus>(status, HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerGetStatus>(status,
					HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/customer")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<CustomerGetAllStatus> customerList() {
		CustomerGetAllStatus status = new CustomerGetAllStatus();
		try {
			List<Customer> allCustomer = customerService.getAll();
			status.setAllCustomer(allCustomer);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("List of Coustomers");
			return new ResponseEntity<CustomerGetAllStatus>(status,
					HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerGetAllStatus>(status,
					HttpStatus.NOT_FOUND);
		}

	}

	//
	@PutMapping("/customer")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomerGetStatus> updateCustomer(
			@RequestBody Customer customer) {
		CustomerGetStatus status = new CustomerGetStatus();
		try {
			customerService.update(customer);
			status.setCustomer(customer);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Update Done");
			return new ResponseEntity<CustomerGetStatus>(status, HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerGetStatus>(status,
					HttpStatus.NOT_FOUND);
		}

	}
	//
	// @DeleteMapping(value = "providers/{id}")
	// public String provDelete(@PathVariable longx id) {
	// bikeService.removeBikeByProvId(id);
	// providerService.removeProvider(id);
	// return "success";
	// }

	// test commit -ankit
}
