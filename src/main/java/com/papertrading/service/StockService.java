package com.papertrading.service;

import java.util.List;

import com.papertrading.dto.response.StockResponse;
import com.papertrading.model.Stock;

public interface StockService {
	List<StockResponse> getStocks();
	Stock getStockBySymbol(String symbol);
	Double getStockPrice(String symbol);
}
