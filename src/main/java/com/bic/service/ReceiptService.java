package com.bic.service;

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
@Transactional
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
}

//String allCylinderStrJSON = receipt.getAllCylinders();
//
//Gson gson = new Gson();
//CylinderStockQty[] allCylinderStock = gson.fromJson(allCylinderStrJSON, CylinderStockQty[].class);
//if (allCylinderStrJSON == null || allCylinderStock.length <= 0)
//    throw new ReceiptServiceException("No cylinders found");
//for (CylinderStockQty cylinderStockQty : allCylinderStock) {
//    // check if cylinderId is valid call db
//    // if not throw error
//    // check if qty is valid -
//}
//
//boolean isStockSaved;
//for (CylinderStockQty cylinderStockQty : allCylinderStock) {
//    Customer customer = receipt.getCustomer();
//
//    Cylinder cylinder = new Cylinder();
//    cylinder.setCylinderId(cylinderStockQty.getCylinderId());
//
//    int cylinderStock = cylinderStockQty.getCylinderStock();
//
//    ReceiptType receiptType = receipt.getReceiptType();
//    isStockSaved = stockRepository.save(customer, cylinder, cylinderStock, receiptType);
//
//}

//check recepitid - if present throw error
// check er or dr ...if not throw error
// check valid customer id - if id not present throw error
// first save stocks(pass customer, json of cylinder and qty)...if error throw
// exception dont
// if any cylinder or json not perfect or negative value....rollback and
// throw error
// if save stocks true....then save receipt - if error throw