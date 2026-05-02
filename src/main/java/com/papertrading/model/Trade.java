package com.papertrading.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

	private String id;
	private String userId;
	private String symbol;
	private int quantity;
	private double price;
	private double amount;
	private TradeSide side;
	private Instant createdAt;
}
