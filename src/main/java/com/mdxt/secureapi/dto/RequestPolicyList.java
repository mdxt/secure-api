package com.mdxt.secureapi.dto;

import lombok.Data;

@Data
public class RequestPolicyList {
	private GenderEnum gender;
	private Integer age;
	private Boolean tobaccoUser;
	private IncomeRangeEnum incomeRange;
	private OccupationTypeEnum occupationType;
	private QualificationLevelEnum qualificationLevel;
	
	//filters
	private Long coverValue;
	private Integer coverTillAge;
	private Boolean payMonthly;
}
