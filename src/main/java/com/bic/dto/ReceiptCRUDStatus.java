package com.bic.dto;

import com.bic.entity.Receipt;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptCRUDStatus extends Status {

	private Receipt receipt;

}
