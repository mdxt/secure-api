package com.mdxt.secureapi.dto.request;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.IncomeRangeEnum;
import com.mdxt.secureapi.enums.OccupationTypeEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;
import com.mdxt.secureapi.enums.QualificationLevelEnum;

import lombok.Data;

@Data
@MappedSuperclass
public class RequestLifeInsurancePolicyList {
	
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	private Integer age;
	
	private Boolean tobaccoUser;
	
	@Enumerated(EnumType.STRING)
	private IncomeRangeEnum incomeRange;
	
	@Enumerated(EnumType.STRING)
	private OccupationTypeEnum occupationType;
	
	@Enumerated(EnumType.STRING)
	private QualificationLevelEnum qualificationLevel;
	
	//filters
	private Long coverValue;
	private Integer coverTillAge;
	
	@Enumerated(EnumType.STRING)
	private PaymentPeriodEnum paymentPeriod;
}
