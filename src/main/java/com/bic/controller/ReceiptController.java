package com.bic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.ReceiptCRUDStatus;
import com.bic.dto.ReceiptGetAllStatus;
import com.bic.dto.Status.StatusType;
import com.bic.dto.ValidationErrorStatus;
import com.bic.entity.Receipt;
import com.bic.service.ReceiptService;

@RestController
@CrossOrigin
@Validated
public class ReceiptController {

	@Autowired
	private ReceiptService receiptService;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorStatus> handleValidationExceptions(
			MethodArgumentNotValidException e) {
		ValidationErrorStatus status = new ValidationErrorStatus();
		status.setStatus(StatusType.FAILURE);
		status.setMessage("Validation error.");
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		status.setErrors(errors);
		return new ResponseEntity<ValidationErrorStatus>(status,
				HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationErrorStatus> handleConstraintViolationException(
			ConstraintViolationException e) {
		ValidationErrorStatus status = new ValidationErrorStatus();
		status.setStatus(StatusType.FAILURE);
		status.setMessage("Validation error.");
		Map<String, String> errors = new HashMap<>();
		e.getConstraintViolations().forEach((error) -> {
			String fieldName = error.getPropertyPath().toString();
			String errorMessage = error.getMessageTemplate();
			errors.put(fieldName, errorMessage);
		});

		status.setErrors(errors);
		return new ResponseEntity<ValidationErrorStatus>(status,
				HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/receipt")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ReceiptCRUDStatus> saveReceipt(
			@Validated @RequestBody Receipt receipt) {
		ReceiptCRUDStatus status = new ReceiptCRUDStatus();

		try {
			Receipt outputReceipt = receiptService.saveReceipt(receipt);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Receipt created successfuly!");
			status.setReceipt(outputReceipt);
			return new ResponseEntity<ReceiptCRUDStatus>(status,
					HttpStatus.CREATED);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<ReceiptCRUDStatus>(status,
					HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/receipt/{receiptId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ReceiptCRUDStatus> deleteReceipt(
			@PathVariable int receiptId,
			@RequestParam("currDateTime") String currDateTime) {
		ReceiptCRUDStatus status = new ReceiptCRUDStatus();

		try {
			Receipt outputReceipt = receiptService.deleteReceipt(receiptId,
					currDateTime);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Receipt deleted successfully!");
			status.setReceipt(outputReceipt);
			return new ResponseEntity<ReceiptCRUDStatus>(status,
					HttpStatus.CREATED);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<ReceiptCRUDStatus>(status,
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/receipt")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ReceiptGetAllStatus> receiptList(
			@RequestParam("receiptType") @Pattern(regexp = "^[ED]R$", message = "Invalid receipt type. A valid receipt type is ER or DR.") @NotBlank(message = "Receipt type cannot be blank.") String receiptType,
			@RequestParam(name = "customerId", required = false) String customerId,
			@RequestParam("fromDateTime") String fromDateTime,
			@RequestParam("toDateTime") String toDateTime,
			@RequestParam(name = "receiptStatus", required = false) Boolean receiptStatus,
			@RequestParam(name = "pageNo", required = false) String pageNo,
			@RequestParam(name = "size", required = false) String size) {
		ReceiptGetAllStatus status = new ReceiptGetAllStatus();
		try {
			List<Receipt> lr = receiptService.getReceiptList(receiptStatus,
					receiptType, customerId, fromDateTime, toDateTime, pageNo,
					size);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Receipts found.");
			status.setAllReceipt(lr);
			return new ResponseEntity<ReceiptGetAllStatus>(status,
					HttpStatus.OK);
		} catch (Exception e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<ReceiptGetAllStatus>(status,
					HttpStatus.NOT_FOUND);
		}
	}

}
