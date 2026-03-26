package com.papertrading.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {
	private String userId;
	private Double cashBalance;
	private List<HoldingResponse> holdings;
}
