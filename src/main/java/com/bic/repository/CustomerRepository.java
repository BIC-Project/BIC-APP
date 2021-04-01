package com.bic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	public Boolean existsByCustomerName(
			String customerName );
	
	public List<Customer> findAllByOrderByCustomerNameAsc(
			);
	
}
