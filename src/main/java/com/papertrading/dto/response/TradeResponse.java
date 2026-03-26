package com.papertrading.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse {
	private String id;
	private String symbol;
	private int quantity;
	private Double price;
	private Double amount;
	private String side;
	private String executedAt;
	private String message;
}
