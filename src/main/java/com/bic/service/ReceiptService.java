package com.bic.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.dto.CylinderStockQty;
import com.bic.entity.Customer;
import com.bic.entity.Receipt;
import com.bic.entity.ReceiptType;
import com.bic.exception.ReceiptServiceException;
import com.bic.repository.CustomerRepository;
import com.bic.repository.CylinderRepository;
import com.bic.repository.ReceiptRepository;
import com.bic.repository.StockRepository;
import com.google.gson.Gson;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CylinderRepository cylinderRepository;

    public int saveReceipt(Receipt receipt) {
	if (receiptRepository.existsById(receipt.getReceiptId()))
	    throw new ReceiptServiceException("Receipt Id Already Exists!");
	System.out.println(receipt.getReceiptType());
	if (receipt.getReceiptType() != ReceiptType.ER && receipt.getReceiptType() != ReceiptType.DR)
	    throw new ReceiptServiceException("Receipt Type Error!");
	if (!(customerRepository.isCustomerPresent(receipt.getCustomer().getCustomerId())))
	    throw new ReceiptServiceException("Illegal Customer For Receipt!");
	if (!customerRepository.isCustomerActive(receipt.getCustomer().getCustomerId()))
	    throw new ReceiptServiceException(
		    "Selected Customer is Inactive! Please activate customer befor selecting");

	Customer customer = receipt.getCustomer();
	ReceiptType receiptType = receipt.getReceiptType();
	String allCylinderStrJSON = receipt.getAllCylinders();
	System.out.println(allCylinderStrJSON + "dfgfds");
	if (allCylinderStrJSON == null || allCylinderStrJSON.trim().isEmpty())
	    throw new ReceiptServiceException("No Cylinders Found");
	Gson gson = new Gson();
	CylinderStockQty[] allCylinderStock = gson.fromJson(allCylinderStrJSON, CylinderStockQty[].class);
	if (allCylinderStock.length <= 0)
	    throw new ReceiptServiceException("No Cylinders Found");
	for (CylinderStockQty cylinderStockQty : allCylinderStock) {
	    int cylinderId = cylinderStockQty.getCylinderId();
	    if (!cylinderRepository.existsById(cylinderId))
		throw new ReceiptServiceException("Illegal Cylinder Id");
	    if (cylinderStockQty.getCylinderStock() <= 0)
		throw new ReceiptServiceException("Illegal Cylinder Qty");
	}
	boolean isStockSaved = stockRepository.saveAll(allCylinderStock, customer, receiptType);
	if (isStockSaved)
	    receiptRepository.save(receipt);
	else
	    throw new ReceiptServiceException("Error Saving Receipt. Please Try Again!");
	return receipt.getReceiptId();

    }

    public List<Receipt> getReceiptList(String receiptType, String customerId, String fromDateTime, String toDateTime) {
	if (receiptType == null)
	    throw new ReceiptServiceException("Receipt Type not selected! Please select a valid receipt type");

	if (!receiptType.equals(ReceiptType.ER.toString()) && !receiptType.equals(ReceiptType.DR.toString()))
	    throw new ReceiptServiceException("Receipt Type Error!");

	if (fromDateTime == null || fromDateTime.trim().isEmpty() || toDateTime == null || toDateTime.trim().equals(""))
	    throw new ReceiptServiceException("Date is empty! Please select appropriate Date!");
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
	    throw new ReceiptServiceException("Invalid date. Please select appropriate date!");
	}

	if (fromDate.compareTo(toDate) > 0)
	    throw new ReceiptServiceException("Invalid Date! End-date greater than start-date.");

	if (customerId == null || customerId.trim().isEmpty() ) {
	    return receiptRepository.findByReceiptTypeAndDateTimeBetweenOrderByDateTimeDesc(
		    ReceiptType.valueOf(receiptType), fromDate, toDate);
	}
	try {
	    Integer custId = Integer.parseInt(customerId);
	    if (!customerRepository.isCustomerPresent(custId))
		throw new ReceiptServiceException();
	    Customer customer = new Customer();
	    customer.setCustomerId(custId);
	    return receiptRepository.findByReceiptTypeAndCustomerAndDateTimeBetweenOrderByDateTimeDesc(
		    ReceiptType.valueOf(receiptType), customer, fromDate, toDate);
	} catch (Exception e) {
	    throw new ReceiptServiceException("Invalid Customer ID!");
	}
    }
}

//if (customerId == null)
//{
//    
//}
//    if (!customerRepository.isCustomerPresent(customerId))
//	throw new ReceiptServiceException("Invalid Customer!");
//
//Customer customer = new Customer();
//customer.setCustomerId(customerId);
