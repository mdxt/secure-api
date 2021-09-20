package com.mdxt.secureapi.dto.response;

import lombok.Data;

@Data
public class BasePolicyListResponse {
	private Long id;
	
	private String insurer;
	private String name;
	
	//the total amount covered has an upper and lower limit
	//for calculating the cost, the multiplier is applied to the cover value
	private Long minCoverValue;
	private Long maxCoverValue;
	
	//policy cover for x number of people, min x==1
//	private Integer maxNumberCovered;
//	private Double multiplierNumberCOvered;
	
	//age till which the policy applies
	private Integer minCoverTillAge;
	private Integer maxCoverTillAge;
	
	private String[] additionalFeatures;
	
	private Double cost;
}
