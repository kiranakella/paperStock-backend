package com.papertrading.service;

import com.papertrading.dto.request.TradeRequest;
import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.dto.response.TradeResponse;

public interface TradeService {
	TradeResponse buyStock(String username, TradeRequest request);
	TradeResponse sellStock(String username, TradeRequest request);
	PortfolioResponse getPortfolio(String username);
}
