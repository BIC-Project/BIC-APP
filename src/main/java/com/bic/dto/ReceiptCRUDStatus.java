package com.bic.dto;

import com.bic.entity.Receipt;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ReceiptCRUDStatus extends Status {

	private Receipt receipt;

}
