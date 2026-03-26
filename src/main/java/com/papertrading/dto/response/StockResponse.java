package com.papertrading.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
	private String symbol;
	private String name;
	private String exchange;
	private Double price;
}
