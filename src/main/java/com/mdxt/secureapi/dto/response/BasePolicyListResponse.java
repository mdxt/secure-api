package com.mdxt.secureapi.dto.response;

import lombok.Data;

@Data
public class BasePolicyListResponse {
	private Long id;
	
	private String insurer;
	private String name;
	
	private String[] additionalFeatures;
	
	private Double cost;
}
