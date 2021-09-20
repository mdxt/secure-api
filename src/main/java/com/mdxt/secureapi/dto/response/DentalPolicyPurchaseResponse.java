package com.mdxt.secureapi.dto.response;

import lombok.Data;

@Data
public class DentalPolicyPurchaseResponse extends BasePolicyListResponse{
	private Long deductible;
}
