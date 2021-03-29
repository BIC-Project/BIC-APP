package com.bic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.ReceiptCreateStatus;
import com.bic.dto.ReceiptGetAllStatus;
import com.bic.dto.Status.StatusType;
import com.bic.entity.Receipt;
import com.bic.exception.ReceiptServiceException;
import com.bic.service.ReceiptService;

@RestController
@CrossOrigin
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/receipt")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReceiptCreateStatus> saveReceipt(@RequestBody Receipt receipt) {

	try {
	    int id = receiptService.saveReceipt(receipt);
	    ReceiptCreateStatus status = new ReceiptCreateStatus();
	    status.setStatus(StatusType.SUCCESS);
	    status.setMessage("Receipt Created Successfuly!");
	    status.setReceiptId(id);
	    return new ResponseEntity<ReceiptCreateStatus>(status, HttpStatus.CREATED);
	} catch (ReceiptServiceException e) {
	    e.printStackTrace();
	    ReceiptCreateStatus status = new ReceiptCreateStatus();
	    status.setStatus(StatusType.FAILURE);
	    status.setMessage(e.getMessage());
	    return new ResponseEntity<ReceiptCreateStatus>(status, HttpStatus.CONFLICT);
	}
    }

    @GetMapping("/receipt")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReceiptGetAllStatus> receiptList(@RequestParam("receiptType") String receiptType,
	    @RequestParam("customerId") String customerId, @RequestParam("fromDateTime") String fromDateTime,
	    @RequestParam("toDateTime") String toDateTime) {
	try {
	    ReceiptGetAllStatus status = new ReceiptGetAllStatus();
	    List<Receipt> lr = receiptService.getReceiptList(receiptType, customerId, fromDateTime, toDateTime);
	    status.setStatus(StatusType.SUCCESS);
	    status.setMessage("Receipts fetched Successfully!");
	    status.setAllReceipt(lr);
	    return new ResponseEntity<ReceiptGetAllStatus>(status, HttpStatus.OK);
	} catch (Exception e) {
	    ReceiptGetAllStatus status = new ReceiptGetAllStatus();
	    status.setStatus(StatusType.FAILURE);
	    status.setMessage(e.getMessage());
	    return new ResponseEntity<ReceiptGetAllStatus>(status, HttpStatus.CONFLICT);
	}
    }

    // modify challan
}
