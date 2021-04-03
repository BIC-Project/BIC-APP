package com.bic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bic.entity.CompositeLocationCustomerCylinder;
import com.bic.entity.Customer;
import com.bic.entity.Location;
import com.bic.entity.Stock;

public interface StockJPARepository
		extends
			JpaRepository<Stock, CompositeLocationCustomerCylinder> {

	@Query("SELECT s FROM Stock s WHERE s.compositeCustomerCylinderId.location = :location and s.compositeCustomerCylinderId.customer = :customer ORDER BY"
			+ "  s.compositeCustomerCylinderId.cylinder.cylinderId")
	public List<Stock> findAllStocksByLocationByCustomer(
			@Param("location") Location location,
			@Param("customer") Customer customer);

	@Query("SELECT s FROM Stock s WHERE s.compositeCustomerCylinderId.location = :location")
	public Page<Stock> findAllStocksByLocation(
			@Param("location") Location location, Pageable pageble);
}
