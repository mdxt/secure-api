package com.mdxt.secureapi.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;

import lombok.Data;

@Data
@MappedSuperclass
public class RequestDentalPolicyList {
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	private Integer numberCovered;
	private Integer age;
	private Integer zipCode;
	
	//filters
	private Long coverValue;
	private Integer coverPeriod;
	
	@Enumerated(EnumType.STRING)
	private PaymentPeriodEnum paymentPeriod;
}
