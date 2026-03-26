package com.papertrading.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("trades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

	@Id
	private String id;
	private String userId;
	private String symbol;
	private int quantity;
	private double price;
	private double amount;
	private TradeSide side;
	private Instant createdAt;
}
