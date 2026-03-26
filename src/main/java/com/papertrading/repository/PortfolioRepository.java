package com.papertrading.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.papertrading.model.Portfolio;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
	Optional<Portfolio> findByUserId(String userId);
}
