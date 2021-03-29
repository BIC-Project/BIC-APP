package com.bic.dto;

public class ReceiptCreateStatus extends Status {

    private int receiptId;

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
