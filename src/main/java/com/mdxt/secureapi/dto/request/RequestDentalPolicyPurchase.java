package com.mdxt.secureapi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RequestDentalPolicyPurchase extends RequestDentalPolicyList{
	
	@NotNull
	private Long policyId;
	
	@NotBlank
	private String buyerName;
	
	@NotBlank
	private String address;
	
	@NotNull
	private Double cost;
}
