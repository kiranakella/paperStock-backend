package com.papertrading.service;

import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.model.Portfolio;
import com.papertrading.model.User;

public interface PortfolioService {
	PortfolioResponse getPortfolio(String username);
	Portfolio getOrCreatePortfolio(User user);
	Portfolio savePortfolio(Portfolio portfolio);
}
