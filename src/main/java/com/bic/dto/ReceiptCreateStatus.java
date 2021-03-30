package com.bic.dto;

import lombok.Data;

@Data
public class ReceiptCreateStatus extends Status {

	private int receiptId;
	private String receiptNo;

	public ReceiptCreateStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReceiptCreateStatus(int receiptId) {
		super();
		this.receiptId = receiptId;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

}
