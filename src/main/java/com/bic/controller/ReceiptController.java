package com.bic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.ReceiptCreateStatus;
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
	    System.out.println("Hello");
	    System.out.println(receipt);
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

//    @GetMapping("/receipt")
//	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//	public ResponseEntity<List<Receipt>> customerList(){
//		return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
//	}
}
