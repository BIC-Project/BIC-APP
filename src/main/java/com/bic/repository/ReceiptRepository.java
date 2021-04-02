package com.bic.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Customer;
import com.bic.entity.Receipt;
import com.bic.entity.ReceiptType;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

	public Receipt findByReceiptId(int receiptId);

	public Page<Receipt> findByReceiptStatusAndReceiptTypeAndCustomerAndDateTimeBetween(
			boolean receiptStatus, ReceiptType receiptType, Customer customer,
			Date start, Date end, Pageable pageble);

	public Page<Receipt> findByReceiptStatusAndReceiptTypeAndDateTimeBetween(
			boolean receiptStatus, ReceiptType receiptType, Date start,
			Date end, Pageable pageble);

}
