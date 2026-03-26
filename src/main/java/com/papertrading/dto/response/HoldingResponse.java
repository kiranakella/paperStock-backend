package com.papertrading.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingResponse {
	private String symbol;
	private int quantity;
	private Double averagePrice;
	private Double currentPrice;
	private Double marketValue;
}
