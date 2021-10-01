package com.mdxt.secureapi.dto.request;

import javax.validation.constraints.NotBlank;

import com.mdxt.secureapi.enums.ExaminationTypeEnum;

import lombok.Data;

@Data
public class RequestCreateLifeInsurancePolicy extends RequestCreateBasePolicy{
	
	private Long minCoverValue;
	private Long maxCoverValue;
	
	private Double multiplierCoverValue;
	
	private Integer minCoverTillAge;
	private Integer maxCoverTillAge;
	
	private Double multiplierCoverTillAge;
	
	private ExaminationTypeEnum examinationType;
}
