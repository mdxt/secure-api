package com.mdxt.secureapi.dto.request;

import lombok.Data;

@Data
public class RequestLifeInsurancePolicyPurchase extends RequestLifeInsurancePolicyList{
	private Long policyId;
	
	private String buyerName;
	
	private String address;
	
	private Double cost;
}
