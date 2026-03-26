package com.papertrading.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.papertrading.dto.response.StockResponse;
import com.papertrading.model.Stock;
import com.papertrading.repository.StockRepository;
import com.papertrading.service.StockService;
import com.papertrading.util.AppConstants;
import com.papertrading.util.DtoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;

	@Override
	public List<StockResponse> getStocks() {
		return stockRepository.findAll().stream()
				.map(DtoMapper::toStockResponse)
				.toList();
	}

	@Override
	public Stock getStockBySymbol(String symbol) {
		return stockRepository.findBySymbol(symbol)
				.orElseThrow(() -> new IllegalArgumentException("Unknown stock: " + symbol));
	}

	@Override
	@Cacheable(cacheNames = AppConstants.STOCK_PRICE_CACHE, key = "#symbol")
	public Double getStockPrice(String symbol) {
		return getStockBySymbol(symbol).getPrice();
	}
}
