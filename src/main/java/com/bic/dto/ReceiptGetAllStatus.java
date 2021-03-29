package com.bic.dto;

import java.util.List;

import com.bic.entity.Receipt;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptGetAllStatus extends Status {

    List<Receipt> allReceipt;
}
