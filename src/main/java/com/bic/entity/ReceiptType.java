package com.bic.entity;

import javax.validation.constraints.Pattern;

@Pattern(regexp = "^[ED]R$", message = "Invalid receipt type. A valid receipt type is ER or DR.")
public enum ReceiptType {
	ER, DR

}
