package com.bic.dto;

import org.springframework.data.domain.Page;

import com.bic.entity.Receipt;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptGetAllStatus extends Status {

	Page<Receipt> allReceipt;
}
