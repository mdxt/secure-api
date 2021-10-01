package com.mdxt.secureapi.dto.request;

import javax.validation.constraints.NotBlank;

import com.mdxt.secureapi.enums.ExaminationTypeEnum;

import lombok.Data;

@Data
public class RequestCreateBasePolicy {

	@NotBlank
	private String insurer;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String documentPath;
	
	@NotBlank
	private String additionalFeaturesCSV;

}