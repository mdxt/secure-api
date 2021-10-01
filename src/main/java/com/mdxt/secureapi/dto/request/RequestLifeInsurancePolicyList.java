package com.mdxt.secureapi.dto.request;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.IncomeRangeEnum;
import com.mdxt.secureapi.enums.OccupationTypeEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;
import com.mdxt.secureapi.enums.QualificationLevelEnum;

import lombok.Data;

@Data
@MappedSuperclass
public class RequestLifeInsurancePolicyList {
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	@NotNull
	@Min(18)
	private Integer age;
	
	@NotNull
	private Boolean tobaccoUser;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private IncomeRangeEnum incomeRange;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private OccupationTypeEnum occupationType;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private QualificationLevelEnum qualificationLevel;
	
	//filters
	
	@NotNull
	@Min(50000)
	private Long coverValue;
	
	@NotNull
	@Min(18)
	private Integer coverTillAge;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private PaymentPeriodEnum paymentPeriod;
}
