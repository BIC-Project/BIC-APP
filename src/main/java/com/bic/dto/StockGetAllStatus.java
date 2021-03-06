package com.bic.dto;

import org.springframework.data.domain.Page;

import com.bic.entity.Stock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StockGetAllStatus extends Status {
	private Page<Stock> allStocks;
}
