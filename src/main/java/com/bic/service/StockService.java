package com.bic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.entity.Customer;
import com.bic.entity.Stock;
import com.bic.exception.StockServiceException;
import com.bic.repository.CustomerRepository;
import com.bic.repository.StockJPARepository;

@Service
@Transactional
public class StockService {

	@Autowired
	private StockJPARepository stockJPARepository;

	@Autowired
	private CustomerRepository customerRepository;

	public List<Stock> getbyCustomerId(int customerId) {

		if (!customerRepository.existsById((customerId)))
			throw new StockServiceException("Invalid customer id.");

		Optional<Customer> customer = customerRepository.findById(customerId);

		List<Stock> stocklst = stockJPARepository
				.findAllStocksByCustomer(customer.get());
		if (stocklst == null || stocklst.size() <= 0)
			throw new StockServiceException(
					"No stocks found for given customer.");
		return stocklst;
	}

	public Page<Stock> getAll(String pageNoStr, String sizeStr) {

		int pageNo = 0;
		int size = 10;
		try {
			if (pageNoStr != null && !pageNoStr.trim().isEmpty())
				pageNo = Math.abs(Integer.parseInt(pageNoStr));
			if (sizeStr != null && sizeStr.trim().isEmpty())
				size = Math.abs(Integer.parseInt(pageNoStr));
		} catch (Exception e) {
			throw new StockServiceException("Invalid page no./page size.");
		}

		Pageable page = PageRequest.of(pageNo, size, Sort
				.by("compositeCustomerCylinderId.customer.customerName")
				.and(Sort.by(
						"compositeCustomerCylinderId.cylinder.cylinderId")));
		Page<Stock> stocklst = stockJPARepository.findAllStocks(page);
		if (stocklst == null || !stocklst.hasContent())
			throw new StockServiceException("No stocks found.");
		return stocklst;
	}
}
