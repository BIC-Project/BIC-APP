package com.bic.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Customer;
import com.bic.entity.Receipt;
import com.bic.entity.ReceiptType;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    public Receipt findByReceiptId(int receiptId);

    // public List<Receipt> findByReceiptTypeAndCustomer(ReceiptType receiptType,
    // Customer customer);

    public List<Receipt> findByReceiptTypeAndCustomerAndDateTimeBetweenOrderByDateTimeDesc(ReceiptType receiptType,
	    Customer customer, Date start, Date end);

    public List<Receipt> findByReceiptTypeAndDateTimeBetweenOrderByDateTimeDesc(ReceiptType receiptType, Date start,
	    Date end);

}
