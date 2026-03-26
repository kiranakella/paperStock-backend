package com.papertrading.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.papertrading.model.Trade;

public interface TradeRepository extends MongoRepository<Trade, String> {
	List<Trade> findByUserId(String userId);
}
