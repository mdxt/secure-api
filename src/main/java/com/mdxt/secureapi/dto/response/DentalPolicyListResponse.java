package com.mdxt.secureapi.dto.response;


import lombok.Data;

@Data
public class DentalPolicyListResponse extends BasePolicyListResponse{
	private Long[] coverValues;
	
	private Integer minCoverPeriod;
	private Integer maxCoverPeriod;
	
	private Integer minNumberCovered;
	private Integer maxNumberCovered;
	private Long deductible;
}
