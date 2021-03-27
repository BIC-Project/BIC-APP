package com.bic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public CustomerRegisterStatus register(@RequestBody Customer customer) {
	System.out.println("here");
	try {
	    int id = customerService.register(customer);
	    CustomerRegisterStatus status = new CustomerRegisterStatus();
	    status.setStatus(StatusType.SUCCESS);
	    status.setMessage("Registration Successful!");
	    status.setRegisteredCustomerId(id);
	    return status;
	} catch (CustomerServiceException e) {
	    CustomerRegisterStatus status = new CustomerRegisterStatus();
	    status.setStatus(StatusType.FAILURE);
	    status.setMessage(e.getMessage());
	    return status;
	}
    }

    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable int customerId) {
	System.out.println("in cntr");
	System.out.println("id : " + customerId);
	return customerService.get(customerId);
    }

//	@GetMapping(value = "customer")
//	public List<Customer> customerList(){
//		return customerService.getAll();
//	}
//	
//	@PutMapping(value = "providers")
//	public Provider provUpdate(@RequestBody Provider provider) throws Exception {
//		
//		return providerService.modifyProvider(provider);
//	}
//	
//	@DeleteMapping(value = "providers/{id}")
//	public String provDelete(@PathVariable long id) {
//		bikeService.removeBikeByProvId(id);
//		providerService.removeProvider(id);
//		return "success";
//	}

    // test commit -ankit
}
//yash-test