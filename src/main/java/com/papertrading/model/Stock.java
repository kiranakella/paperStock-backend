package com.papertrading.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("stocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

	@Id
	private String id;
	private String symbol;
	private String name;
	private String exchange;
	private Double price;
}
