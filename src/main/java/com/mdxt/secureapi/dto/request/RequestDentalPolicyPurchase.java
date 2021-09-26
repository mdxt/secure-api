package com.mdxt.secureapi.dto.request;

import lombok.Data;

@Data
public class RequestDentalPolicyPurchase extends RequestDentalPolicyList{
	private Long policyId;
	
	private String buyerName;
	
	private String address;
	
	private Double cost;
}
