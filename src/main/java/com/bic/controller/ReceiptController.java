package com.bic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.ReceiptCreateModifyStatus;
import com.bic.dto.ReceiptGetAllStatus;
import com.bic.dto.Status.StatusType;
import com.bic.entity.Receipt;
import com.bic.service.ReceiptService;

@RestController
@CrossOrigin
public class ReceiptController {

	@Autowired
	private ReceiptService receiptService;

	@PostMapping("/receipt")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ReceiptCreateModifyStatus> saveReceipt(
			@RequestBody Receipt receipt) {

		try {
			Receipt outputReceipt = receiptService.saveReceipt(receipt);
			ReceiptCreateModifyStatus status = new ReceiptCreateModifyStatus();

			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Receipt Created Successfuly!");
			status.setReceipt(outputReceipt);
			return new ResponseEntity<ReceiptCreateModifyStatus>(status,
					HttpStatus.CREATED);
		} catch (Exception e) {
			ReceiptCreateModifyStatus status = new ReceiptCreateModifyStatus();
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<ReceiptCreateModifyStatus>(status,
					HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/receipt")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ReceiptGetAllStatus> receiptList(
			@RequestParam("receiptType") String receiptType,
			@RequestParam(name = "customerId", required = false) String customerId,
			@RequestParam("fromDateTime") String fromDateTime,
			@RequestParam("toDateTime") String toDateTime,
			@RequestParam(name = "pageNo", required = false) String pageNo,
			@RequestParam(name = "size", required = false) String size) {
		try {
			ReceiptGetAllStatus status = new ReceiptGetAllStatus();
			List<Receipt> lr = receiptService.getReceiptList(receiptType,
					customerId, fromDateTime, toDateTime, pageNo, size);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Receipts fetched Successfully!");
			status.setAllReceipt(lr);
			return new ResponseEntity<ReceiptGetAllStatus>(status,
					HttpStatus.OK);
		} catch (Exception e) {
			ReceiptGetAllStatus status = new ReceiptGetAllStatus();
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<ReceiptGetAllStatus>(status,
					HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/receipt")
	@PreAuthorize("hasAnyRole('ADMIN')")
	ResponseEntity<ReceiptCreateModifyStatus> updateReceipt(
			@RequestBody Receipt receipt) {
		try {
			Receipt outputReceipt = receiptService.updateReceipt(receipt);
			ReceiptCreateModifyStatus status = new ReceiptCreateModifyStatus();
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Receipt Created Successfuly!");
			status.setReceipt(outputReceipt);
			return new ResponseEntity<ReceiptCreateModifyStatus>(status,
					HttpStatus.CREATED);
		} catch (Exception e) {
			ReceiptCreateModifyStatus status = new ReceiptCreateModifyStatus();
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<ReceiptCreateModifyStatus>(status,
					HttpStatus.CONFLICT);
		}

	}
}
