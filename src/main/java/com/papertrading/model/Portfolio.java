package com.papertrading.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("portfolios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

	@Id
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
