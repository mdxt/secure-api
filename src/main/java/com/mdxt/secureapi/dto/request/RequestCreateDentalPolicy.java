package com.mdxt.secureapi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RequestCreateDentalPolicy extends RequestCreateBasePolicy{
	
	@NotBlank
	private String coverValuesCSV; 
	
	@NotNull
	private Double multiplierCoverValue;
	
	@NotNull
	private Integer minCoverPeriod;
	
	@NotNull
	private Integer maxCoverPeriod;
	
	@NotNull
	private Double multiplierCoverPeriod;
	
	@NotNull
	private Integer minNumberCovered;
	
	@NotNull
	private Integer maxNumberCovered;
	
	@NotNull
	private Double multiplierNumberCovered;
	
	@NotNull
	private Long deductible;
}
