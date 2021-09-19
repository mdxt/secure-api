package com.mdxt.secureapi.dto.request;

import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;

import lombok.Data;

@Data
public class RequestDentalPolicyList {
	private GenderEnum gender;
	private Integer numberCovered;
	private Integer age;
	private Integer zipCode;
	
	//filters
	private Long coverValue;
	private Integer coverTillAge;
	private PaymentPeriodEnum paymentPeriod;
}
