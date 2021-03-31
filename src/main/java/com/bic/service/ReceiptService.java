package com.bic.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.dto.CylinderStockQty;
import com.bic.entity.Config;
import com.bic.entity.Customer;
import com.bic.entity.Receipt;
import com.bic.entity.ReceiptType;
import com.bic.exception.ReceiptServiceException;
import com.bic.repository.ConfigRepository;
import com.bic.repository.CustomerRepository;
import com.bic.repository.CylinderRepository;
import com.bic.repository.ReceiptRepository;
import com.bic.repository.StockRepository;
import com.google.gson.Gson;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiptService {


	@Value("${app.max.receipt.no}")
	private Integer maxReceiptNo;
	@Value("${app.config.receiptNoCountER}")
	private String receiptNoCountER;

	@Value("${app.config.receiptNoCountDR}")
	private String receiptNoCountDR;

	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private CylinderRepository cylinderRepository;

	@Autowired
	private ConfigRepository configRepository;

	public Receipt saveReceipt(Receipt receipt) throws InterruptedException {
		if (receiptRepository.existsById(receipt.getReceiptId()))
			throw new ReceiptServiceException("Receipt Id Already Exists!");
		System.out.println(receipt.getReceiptType());
		if (receipt.getReceiptType() != ReceiptType.ER
				&& receipt.getReceiptType() != ReceiptType.DR)
			throw new ReceiptServiceException("Receipt Type Error!");
		if (!(customerRepository
				.isCustomerPresent(receipt.getCustomer().getCustomerId())))
			throw new ReceiptServiceException("Illegal Customer For Receipt!");
		if (!customerRepository
				.isCustomerActive(receipt.getCustomer().getCustomerId()))
			throw new ReceiptServiceException(
					"Selected Customer is Inactive! Please activate customer befor selecting");
		Customer customer = receipt.getCustomer();
		ReceiptType receiptType = receipt.getReceiptType();
		String allCylinderStrJSON = receipt.getAllCylinders();
		System.out.println(allCylinderStrJSON + "dfgfds");
		if (allCylinderStrJSON == null || allCylinderStrJSON.trim().isEmpty())
			throw new ReceiptServiceException("No Cylinders Found");
		Gson gson = new Gson();
		CylinderStockQty[] allCylinderStock = gson.fromJson(allCylinderStrJSON,
				CylinderStockQty[].class);
		if (allCylinderStock.length <= 0)
			throw new ReceiptServiceException("No Cylinders Found");
		for (CylinderStockQty cylinderStockQty : allCylinderStock) {
			int cylinderId = cylinderStockQty.getCylinderId();
			if (!cylinderRepository.existsById(cylinderId))
				throw new ReceiptServiceException("Illegal Cylinder Id");
			if (cylinderStockQty.getCylinderStock() <= 0)
				throw new ReceiptServiceException("Illegal Cylinder Qty");
		}
		boolean isStockSaved = stockRepository.saveAll(allCylinderStock,
				customer, receiptType);
		if (isStockSaved) {
			Config config = null;
			if (receipt.getReceiptType().equals(ReceiptType.ER))
				config = configRepository.findByConfigKey(receiptNoCountER);
			else if (receipt.getReceiptType().equals(ReceiptType.DR))

				config = configRepository.findByConfigKey(receiptNoCountDR);
			if (config == null)
				throw new ReceiptServiceException("Error! Please try again");
			else {
				int currentReceiptNo = Integer
						.parseInt(config.getConfigValue());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(receipt.getDateTime());

				receipt.setReceiptNo("BIC-" + receipt.getReceiptType() + "-"
						+ String.format("%02d",
								(calendar.get(Calendar.MONTH)) + 1)
						+ "-"
						+ String.format("%02d", calendar.get(Calendar.YEAR))
						+ "-"
						+ String.format("%0"
								+ String.valueOf(maxReceiptNo).length() + "d",
								currentReceiptNo));
				System.out.println(receipt.getReceiptNo());
				if (currentReceiptNo >= maxReceiptNo) {
					config.setConfigValue("1");
				} else {
					currentReceiptNo++;
					config.setConfigValue(String.valueOf(currentReceiptNo));
				}
			}
			receiptRepository.save(receipt);
		} else
			throw new ReceiptServiceException(
					"Error Saving Receipt. Please Try Again!");
		return receipt;


	}

	public Receipt updateReceipt(Receipt receipt) throws InterruptedException {

		// receipt -check receipt id ---if not present or null return
		// if receiptno is null error or receipt id is null

		// get old receipt -
		// check receiptno - old receipt number...if missmatch error.

		// check all other like previous

		// string all cylinders-----

		return null;

	}

	public List<Receipt> getReceiptList(String receiptType, String customerId,
			String fromDateTime, String toDateTime, String pageNoStr,
			String sizeStr) {
		if (receiptType == null)
			throw new ReceiptServiceException(
					"Receipt Type not selected! Please select a valid receipt type");

		if (!receiptType.equals(ReceiptType.ER.toString())
				&& !receiptType.equals(ReceiptType.DR.toString()))
			throw new ReceiptServiceException("Receipt Type Error!");

		if (fromDateTime == null || fromDateTime.isBlank() || toDateTime == null
				|| toDateTime.trim().equals(""))
			throw new ReceiptServiceException(
					"Date is empty! Please select appropriate Date!");
		Date fromDate, toDate;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			fromDate = formatter.parse(fromDateTime);
			toDate = formatter.parse(toDateTime);
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.HOUR, 23);
			c.add(Calendar.MINUTE, 59);
			c.add(Calendar.SECOND, 59);
			toDate = c.getTime();
		} catch (Exception e) {
			throw new ReceiptServiceException(
					"Invalid date. Please select appropriate date!");
		}

		if (fromDate.compareTo(toDate) > 0)
			throw new ReceiptServiceException(
					"Invalid Date! End-date greater than start-date.");

		int pageNo = 0;
		int size = 10;
		try {
			if (pageNoStr != null && !pageNoStr.trim().isEmpty()
					&& !pageNoStr.trim().equals("0"))
				pageNo = Math.abs(Integer.parseInt(pageNoStr)) - 1;
			if (sizeStr != null && !sizeStr.trim().isEmpty())
				size = Math.abs(Integer.parseInt(sizeStr));
		} catch (Exception e) {
			throw new ReceiptServiceException("Invalid page No.");
		}
		Pageable page = PageRequest.of(pageNo, size);
		if (customerId == null || customerId.isBlank()) {
			return receiptRepository
					.findByReceiptTypeAndDateTimeBetweenOrderByDateTimeDesc(
							ReceiptType.valueOf(receiptType), fromDate, toDate,
							page);
		}
		try {
			Integer custId = Integer.parseInt(customerId);
			if (!customerRepository.isCustomerPresent(custId))
				throw new ReceiptServiceException();
			Customer customer = new Customer();
			customer.setCustomerId(custId);
			return receiptRepository
					.findByReceiptTypeAndCustomerAndDateTimeBetweenOrderByDateTimeDesc(
							ReceiptType.valueOf(receiptType), customer,
							fromDate, toDate, page);
		} catch (Exception e) {
			throw new ReceiptServiceException("Invalid Customer ID!");
		}
	}
}
