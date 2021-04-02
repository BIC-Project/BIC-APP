package com.bic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.CustomerGetAllStatus;
import com.bic.dto.CustomerGetStatus;
import com.bic.dto.CustomerRegisterStatus;
import com.bic.dto.Status.StatusType;
import com.bic.dto.ValidationErrorStatus;
import com.bic.entity.Customer;
import com.bic.service.CustomerService;

@RestController
@CrossOrigin
@Validated
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorStatus> handleValidationExceptions(
			MethodArgumentNotValidException e) {
		ValidationErrorStatus status = new ValidationErrorStatus();
		status.setStatus(StatusType.FAILURE);
		status.setMessage("Validation error.");
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		status.setErrors(errors);
		return new ResponseEntity<ValidationErrorStatus>(status,
				HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationErrorStatus> handleConstraintViolationException(
			ConstraintViolationException e) {
		ValidationErrorStatus status = new ValidationErrorStatus();
		status.setStatus(StatusType.FAILURE);
		status.setMessage("Validation error.");
		Map<String, String> errors = new HashMap<>();
		e.getConstraintViolations().forEach((error) -> {
			String fieldName = error.getPropertyPath().toString();
			String errorMessage = error.getMessageTemplate();
			errors.put(fieldName, errorMessage);
		});
		status.setErrors(errors);
		return new ResponseEntity<ValidationErrorStatus>(status,
				HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/customer")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<CustomerRegisterStatus> register(
			@Validated @RequestBody Customer customer) {
		CustomerRegisterStatus status = new CustomerRegisterStatus();
		try {
			int id = customerService.register(customer);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Customer registered successfully!");
			status.setRegisteredCustomerId(id);
			return new ResponseEntity<CustomerRegisterStatus>(status,
					HttpStatus.CREATED);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerRegisterStatus>(status,
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/customer/{customerId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<CustomerGetStatus> getCustomer(
			@PathVariable @Positive(message = "Customer id should be positive integer") int customerId) {
		CustomerGetStatus status = new CustomerGetStatus();
		try {
			Customer customer = customerService.get(customerId);
			status.setCustomer(customer);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Customer found.");
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
			status.setMessage("Customer found.");
			return new ResponseEntity<CustomerGetAllStatus>(status,
					HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerGetAllStatus>(status,
					HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/customer")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomerGetStatus> updateCustomer(
			@Validated @RequestBody Customer customer) {
		CustomerGetStatus status = new CustomerGetStatus();
		try {
			customerService.update(customer);
			status.setCustomer(customer);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Customer updated successfully!");
			return new ResponseEntity<CustomerGetStatus>(status, HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<CustomerGetStatus>(status,
					HttpStatus.BAD_REQUEST);
		}
	}
}
