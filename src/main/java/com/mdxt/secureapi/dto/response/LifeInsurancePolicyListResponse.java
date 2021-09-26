package com.mdxt.secureapi.dto.response;

import com.mdxt.secureapi.enums.ExaminationTypeEnum;

import lombok.Data;

@Data
public class LifeInsurancePolicyListResponse extends BasePolicyListResponse{
	//the total amount covered has an upper and lower limit
	//for calculating the cost, the multiplier is applied to the cover value
	private Long minCoverValue;
	private Long maxCoverValue;
	
	//age till which the policy applies
	private Integer minCoverTillAge;
	private Integer maxCoverTillAge;
	private ExaminationTypeEnum examinationType;
}
