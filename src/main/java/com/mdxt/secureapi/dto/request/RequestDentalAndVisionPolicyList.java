package com.mdxt.secureapi.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;

import lombok.Data;

@Data
@MappedSuperclass
public class RequestDentalAndVisionPolicyList {
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	@NotNull
	private Integer numberCovered;
	
	@NotNull
	private Integer age;
	
	@NotNull
	private Integer zipCode;
	
	//filters
	@NotNull
	private Long coverValue;
	
	@NotNull
	private Integer coverPeriod;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private PaymentPeriodEnum paymentPeriod;
}
