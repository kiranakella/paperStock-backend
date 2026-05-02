package com.papertrading.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

	private String id;
	private String userId;
	@Builder.Default
	private Double cashBalance = 0.0;
	@Builder.Default
	private List<Holding> holdings = new ArrayList<>();

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Holding {
		private String symbol;
		private int quantity;
		private double averagePrice;
	}
}
