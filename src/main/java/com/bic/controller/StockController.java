package com.bic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.Status.StatusType;
import com.bic.dto.StockGetAllStatus;
import com.bic.dto.StockGetStatus;
import com.bic.entity.Stock;
import com.bic.service.StockService;

@RestController
@CrossOrigin
public class StockController {

	@Autowired
	private StockService stockService;

	@GetMapping("/stock")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<StockGetAllStatus> getAllStocks(
			@RequestParam(name = "pageNo", required = false) String pageNo,
			@RequestParam(name = "size", required = false) String size) {
		StockGetAllStatus status = new StockGetAllStatus();
		try {
			Page<Stock> allStocks = stockService.getAll(pageNo, size);
			status.setAllStocks(allStocks);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Stocks found.");
			return new ResponseEntity<>(status, HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/stock/{customerId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<StockGetStatus> getStockByCustomer(
			@PathVariable int customerId) {
		StockGetStatus status = new StockGetStatus();
		try {
			List<Stock> allStocks = stockService.getbyCustomerId(customerId);
			status.setAllStocks(allStocks);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Stocks found.");
			return new ResponseEntity<>(status, HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
	}
}
