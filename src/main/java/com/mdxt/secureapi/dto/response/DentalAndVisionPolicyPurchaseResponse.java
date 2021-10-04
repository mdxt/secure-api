package com.mdxt.secureapi.dto.response;

import com.mdxt.secureapi.entity.DentalAndVisionPolicyPurchase;
import com.mdxt.secureapi.entity.DentalPolicyPurchase;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;

import lombok.Data;

@Data
public class DentalAndVisionPolicyPurchaseResponse {
	
	private Long id;
	
	private GenderEnum gender;
	
	private Integer numberCovered;
	private Integer age;
	private Integer zipCode;
	
	//filters
	private Long coverValue;
	private Integer coverPeriod;
	
	private PaymentPeriodEnum paymentPeriod;
	
	private Long policy_id;
	
	private String buyerName;
	
	private String address;
	
	private Double cost;
	
	private ApplicationStateEnum applicationState;
	
	private String username;
	
	private String assigned_underwriter;
	
	public DentalAndVisionPolicyPurchaseResponse(DentalAndVisionPolicyPurchase request) {
		id = request.getId();
		gender = request.getGender();
		age = request.getAge();
		coverValue = request.getCoverValue();
		coverPeriod = request.getCoverPeriod();
		paymentPeriod = request.getPaymentPeriod();
		policy_id = request.getPolicy().getId();
		applicationState = request.getApplicationState();
		buyerName = request.getBuyerName();
		address = request.getAddress();
		cost = request.getCost();
		username = request.getUser().getEmail();
		numberCovered = request.getNumberCovered();
		zipCode = request.getZipCode();
		assigned_underwriter = (request.getAssignedUnderwriter() != null) ? request.getAssignedUnderwriter().getEmail() : "Not Assigned";
	}
}
