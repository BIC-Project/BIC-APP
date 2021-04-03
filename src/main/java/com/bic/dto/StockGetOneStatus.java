package com.bic.dto;

import com.bic.entity.Stock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StockGetOneStatus extends Status {

	private Stock stock;
}
