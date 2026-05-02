package com.papertrading.repository;

import java.util.Optional;

import com.papertrading.model.Portfolio;

public interface PortfolioRepository {
	Optional<Portfolio> findByUserId(String userId);
	Portfolio save(Portfolio portfolio);
}
