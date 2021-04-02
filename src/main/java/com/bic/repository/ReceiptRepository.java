package com.bic.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Customer;
import com.bic.entity.Receipt;
import com.bic.entity.ReceiptType;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

	public Receipt findByReceiptId(int receiptId);

	public List<Receipt> findByReceiptStatusAndReceiptTypeAndCustomerAndDateTimeBetweenOrderByDateTimeDesc(
			boolean receiptStatus, ReceiptType receiptType, Customer customer,
			Date start, Date end, Pageable pageble);

	public List<Receipt> findByReceiptStatusAndReceiptTypeAndDateTimeBetweenOrderByDateTimeDesc(
			boolean receiptStatus, ReceiptType receiptType, Date start,
			Date end, Pageable pageble);

}
