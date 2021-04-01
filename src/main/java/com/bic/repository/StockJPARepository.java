package com.bic.repository;

import java.util.List;

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

	@Query("SELECT * FROM STOCK WHERE CompositeCustomerCylinderId.customer = :customer ORDER BY"
			+ "cylinder.cylinderID")
	public List<Stock> findAllStocksByCustomer(
			@Param("customer") Customer customer);

	@Query("SELECT * FROM STOCK ORDER BY"
			+ "customer.customerName, cylinder.cylinderID")
	public List<Stock> findAllStocks(Pageable pageble);
}
