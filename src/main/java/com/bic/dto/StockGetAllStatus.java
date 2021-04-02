package com.bic.dto;

import java.util.List;

import com.bic.entity.Stock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StockGetAllStatus extends Status {
	private List<Stock> allStocks;
}
