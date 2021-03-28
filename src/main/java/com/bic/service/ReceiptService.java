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
