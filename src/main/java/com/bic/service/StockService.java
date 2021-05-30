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

import com.bic.entity.CompositeLocationCustomerCylinder;
import com.bic.entity.Customer;
import com.bic.entity.Cylinder;
import com.bic.entity.Location;
import com.bic.entity.Stock;
import com.bic.exception.StockServiceException;
import com.bic.repository.CustomerRepository;
import com.bic.repository.CylinderRepository;
import com.bic.repository.LocationRepository;
import com.bic.repository.StockJPARepository;

@Service
@Transactional
public class StockService {

	@Autowired
	private StockJPARepository stockJPARepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CylinderRepository cylinderRepository;

	public List<Stock> getByLocationIdAndCustomerId(int locationId,
			int customerId) {

		if (!locationRepository.existsById(locationId))
			throw new StockServiceException("Invalid location");
		Optional<Location> location = locationRepository.findById(locationId);
		if (!customerRepository.existsById(customerId))
			throw new StockServiceException("Invalid customer.");
		Optional<Customer> customer = customerRepository.findById(customerId);

		List<Stock> stocklst = stockJPARepository
				.findAllStocksByLocationByCustomer(location.get(),
						customer.get());
		if (stocklst == null || stocklst.size() <= 0)
			throw new StockServiceException(
					"No stocks found for given customer at selected location.");
		return stocklst;
	}

	public Page<Stock> getAllByLocationId(int locationId, String pageNoStr,
			String sizeStr) {

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

		if (!locationRepository.existsById(locationId))
			throw new StockServiceException("Invalid location");
		Optional<Location> location = locationRepository.findById(locationId);

		Pageable page = PageRequest.of(pageNo, size, Sort
				.by("compositeCustomerCylinderId.customer.customerName")
				.and(Sort.by(
						"compositeCustomerCylinderId.cylinder.cylinderId")));
		Page<Stock> stocklst = stockJPARepository
				.findAllStocksByLocation(location.get(), page);
		if (stocklst == null || !stocklst.hasContent())
			throw new StockServiceException("No stocks found.");
		return stocklst;
	}

	public Stock getByLocationIdAndCustomerIdAndCylinderId(int locationId,
			int customerId, int cylinderId) {
		if (!locationRepository.existsById(locationId))
			throw new StockServiceException("Invalid location");
		Optional<Location> location = locationRepository.findById(locationId);
		if (!customerRepository.existsById(customerId))
			throw new StockServiceException("Invalid customer.");
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (!cylinderRepository.existsById(cylinderId))
			throw new StockServiceException("Invalid cylinder.");
		Optional<Cylinder> cylinder = cylinderRepository.findById(cylinderId);

		CompositeLocationCustomerCylinder ccId = new CompositeLocationCustomerCylinder(
				location.get(), customer.get(), cylinder.get());
		Optional<Stock> stockOpt = stockJPARepository.findById(ccId);

		if (stockOpt.isEmpty())
			return new Stock(ccId, 0);
		else
			return stockOpt.get();
	}
}
