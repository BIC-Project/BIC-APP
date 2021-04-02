package com.bic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bic.entity.CompositeCustomerCylinder;
import com.bic.entity.Customer;
import com.bic.entity.Stock;

public interface StockJPARepository
		extends
			JpaRepository<Stock, CompositeCustomerCylinder> {

	@Query("SELECT s FROM Stock s WHERE s.compositeCustomerCylinderId.customer = :customer ORDER BY"
			+ "  s.compositeCustomerCylinderId.cylinder.cylinderId")
	public List<Stock> findAllStocksByCustomer(
			@Param("customer") Customer customer);

	@Query("SELECT s FROM Stock s") // ORDER BY")
	// + " s.compositeCustomerCylinderId.customer.customerName,
	// s.compositeCustomerCylinderId.cylinder.cylinderId")
	public Page<Stock> findAllStocks(Pageable pageble);
}
